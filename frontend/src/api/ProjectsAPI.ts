import Util from "../base/Util"

export default class ProjectAPI extends Util{
    api(): import("../base/interfaces/project").Project {
      throw new Error('Method not implemented.');
    }

    private base="/project/"
    // get a list of all projects
    public async getAllProjects(){
        try{
            const projectRequest=await this.fetchRequest(this.base+"get-all-project","GET");
            if(!projectRequest.ok){
                throw new Error('error happened');
            }
            let data=await projectRequest.json();
            return data;
        }catch(err){
            this.throwError(err);
        }
    }
    public async getProject(id:any){
        try{
            const request=await this.fetchRequest(this.base+"get-project/"+id,"get");
            if(!request.ok){
                throw new Error('error happened');
            }
            let data=await request.json();
            return data;
        }catch(err){
            this.throwError(err);
        }
    }
    public async viewCount(id:any){
        try{
            const request=await this.fetchRequest(this.base+"update-project-view/"+id,"PUT");
            if(!request.ok){
                throw new Error('error happened');
            }
            let data=await request.json();
            return data;
        }catch(err){
            this.throwError(err);
        }
    }
    public async repositoryCount(id:any){
        try{
            const request=await this.fetchRequest(this.base+"update-repository-click/"+id,"PUT");
            if(!request.ok){
                throw new Error('error happened');
            }
            let data=await request.json();
            return data;
        }catch(err){
            this.throwError(err);
        }
    }
    public async deleteProject(id:any){
        try{
            const request=await this.fetchRequest(this.base+"delete-project/"+id,"delete");
            return await request;
        }catch(err){
            this.throwError(err);
        }
    }
}