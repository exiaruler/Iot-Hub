package com.scheduler.app.backend.Firmware.Model;

import com.scheduler.Base.ModelBase.ModelBase;

// firmware storage
public class Firmware extends ModelBase{
    // version e.g 1.0.0
    private String version;
    // version numbers
    private int majorVersion;
    // version cycle
    private int minorVersion;
    // patch
    private int patchVersion;
    // firmware version is that latest
    private boolean latest;
    // require update
    private boolean mandatoryUpdate;
    // notes
    private String notes;
    // links to github files
    private String fileLinkA;
    private String fileLinkB;
    private String fileLinkC;

    private void createVersion(){
        String [] arr=version.split(".");
        if(arr.length==3){
            this.majorVersion=Integer.parseInt(arr[0]);
            this.minorVersion=Integer.parseInt(arr[1]);
            this.patchVersion=Integer.parseInt(arr[2]);
        }
    }
    public boolean validateVersion(){
        boolean valid=false;
        String [] arr=version.split(".");
        if(arr.length==3) valid=true;
        return valid;
    }
}
