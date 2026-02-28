import Util from "../../base/Util";

// contains common api
export class aRESTAPI extends Util{
    
    public async getDigitalPins(){
        try{
            const request=await this.serviceRequest("IoT hub","component","digital-pins","GET");
            if(!request.ok){
                throw new Error('error happened');
            }
            let data=await request.json();
            return data;
        }catch(err){
            this.throwError(err);
        }
    }
}