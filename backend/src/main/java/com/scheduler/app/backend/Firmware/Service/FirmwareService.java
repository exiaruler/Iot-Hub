package com.scheduler.app.backend.Firmware.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.scheduler.Base.Exception.ValidationException;
import com.scheduler.Base.Service.BaseService;
import com.scheduler.app.backend.Firmware.Model.Firmware;
import com.scheduler.app.backend.Firmware.Repo.FirmwareRepo;

@Service
public class FirmwareService extends BaseService<Firmware, Long> {
    private static final int CONNECT_TIMEOUT_MILLIS = 10000;
    private static final int READ_TIMEOUT_MILLIS = 120000;

    private final FirmwareRepo firmwareRepo;

    public FirmwareService(FirmwareRepo firmwareRepo) {
        this.firmwareRepo = firmwareRepo;
    }

    @Override
    protected JpaRepository<Firmware, Long> repository() {
        return firmwareRepo;
    }

    @Override
    protected void beforeSave(Firmware entity, Map<String, String> errors, Map<String, String> warnings) {
        if(!entity.validateVersion()){
            errors.put("version", "Invalid firmware version format. Correct format 0.0.0");
        }
        if(entity.getDev() && entity.getMandatoryUpdate()){
            errors.put("mandatoryUpdate", "Development firmware cannot be marked as mandatory update.");
        }
        int exists=getDataInt("select count(id) from firmware where version="+quoteParam(entity.getVersion().trim())+" and id!="+entity.getId());
        if(exists>0){
            errors.put("version", entity.getVersion()+"\s exists already!");
        }
        int existsDev=getDataInt("select count(id) from firmware where dev=true and id!="+entity.getId());
        if(entity.getDev() && existsDev>0){
            errors.put("dev", "Development firmware already exists!");
        }
        if(errors.size()>0){
            throw new ValidationException(errors,null);
        }
        entity.createVersion();
        if(!entity.isDev()){
            Firmware currLatest = getLatestVersion();
            if(currLatest!=null){
                if(compareVersions(entity.getVersion(), currLatest.getVersion()) > 0){
                    currLatest.setLatest(false);
                    entity.setLatest(true);
                    firmwareRepo.save(currLatest);
                }else if(entity.getId()==0) errors.put("version", "version entered is below the latest version \s"+currLatest.getVersion());
            }else
            {
                entity.setLatest(true);
            }
        }
        if(errors.size()>0){
            throw new ValidationException(errors,null);
        }
        String [] linksArr={entity.getMainFile(),entity.getMapFile(),entity.getPartitionFile(),entity.getBootloaderFile()};
        String [] linkNames={"mainFile","mapFile","partitionFile","bootloaderFile"};
        // validate files to ensure they are from github and are binary firmware files
        for(int i=0; i<linksArr.length; i++){
            if(!entity.isDev()){
                validateGithubUrl(linksArr[i], errors, linkNames[i]);
            }
            validateBinFile(linksArr[i], errors, linkNames[i]);
        }
        if(errors.size()>0){
            throw new ValidationException(errors,null);
        }
    }

    private void validateGithubUrl(String url, Map<String, String> errors, String fieldName) {
        if(url == null || url.trim().isEmpty()) {
            errors.put(fieldName, "File URL is required.");
            return;
        }

        try {
            String normalizedUrl = url.trim();
            URI uri = new URI(normalizedUrl);
            String host = uri.getHost();
            String path = uri.getPath();
            boolean isGithubDomain = host != null && (
                    host.equalsIgnoreCase("github.com") ||
                    host.endsWith(".github.com") ||
                    host.equalsIgnoreCase("raw.githubusercontent.com") ||
                    host.endsWith(".githubusercontent.com")
            );
            boolean hasRawPath = path != null && (
                    path.toLowerCase().contains("/raw/") ||
                    host != null && host.equalsIgnoreCase("raw.githubusercontent.com")
            );

            if(!isGithubDomain){
                errors.put(fieldName, "File URL must be hosted on GitHub.");
            } else if(!hasRawPath){
                errors.put(fieldName, "File URL must point to a GitHub raw file path.");
            }
        } catch (Exception e) {
            errors.put(fieldName, "File URL is invalid.");
        }
    }

    private void validateBinFile(String url, Map<String, String> errors, String fieldName) {
        if(url == null || url.trim().isEmpty()) {
            errors.put(fieldName, "File URL is required.");
            return;
        }

        String normalizedUrl = url.trim();
        if(!normalizedUrl.toLowerCase().endsWith(".bin")){
            errors.put(fieldName, "File URL must point to a .bin file.");
        }
    }
    // use to retrieve version
    public Firmware getVersion(String version){
        long exId=getDataInt("select id from firmware where version="+quoteParam(version.trim()));
        Firmware firmware=this.findById(exId);
        return firmware;
    }
    // get the latest firmware
    public Firmware getLatestVersion(){
        long exId=getDataInt("select id from firmware where latest=true limit 1");
        if (exId < 1) {
            return null;
        }
        return this.findById(exId);

    }
    // get mandatory update version that is the latest version
    public Firmware getMandatoryUpdateVersion() {
        long exId=getDataInt("select id from firmware where latest=true and mandatory_update=true limit 1");
        if (exId < 1) {
            return null;
        }
        return this.findById(exId);
    }    

    public Firmware getUpdateVersion(String currentVersion) {
        Firmware latest = getLatestVersion();
        if (latest == null || currentVersion == null || currentVersion.trim().isEmpty()) {
            return latest;
        }
        return compareVersions(latest.getVersion(), currentVersion) > 0 ? latest : null;
    }

    public Firmware getDevelopmentVersion() {
        long exId = getDataLong("select id from firmware where dev=true limit 1");
        if (exId < 1) {
            return null;
        }
        return this.findById(exId);
    }

    public HttpURLConnection openDownload(Firmware firmware) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(firmware.getMainFile()).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
        connection.setReadTimeout(READ_TIMEOUT_MILLIS);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Accept", "application/octet-stream");
        String githubToken = System.getenv("FIRMWARE_GITHUB_TOKEN");
        if (githubToken != null && !githubToken.trim().isEmpty()) {
            connection.setRequestProperty("Authorization", "Bearer " + githubToken.trim());
        }
        return connection;
    }

    public int compareVersions(String left, String right) {
        int[] leftParts = parseVersion(left);
        int[] rightParts = parseVersion(right);
        for (int index = 0; index < leftParts.length; index++) {
            if (leftParts[index] != rightParts[index]) {
                return Integer.compare(leftParts[index], rightParts[index]);
            }
        }
        return 0;
    }

    private int[] parseVersion(String version) {
        if (version == null || !version.trim().matches("\\d+\\.\\d+\\.\\d+")) {
            throw new IllegalArgumentException("Invalid firmware version");
        }
        String[] parts = version.trim().split("\\.");
        return new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]) };
    }

    public void closeDownload(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }

    public List<Firmware> getFirmwares(){
        return repository().findAll();
    }

}
