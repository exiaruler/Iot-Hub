'use client'
import FormHandle from "@/components/form/FormHandle";
import { NextUIBase } from "@/NextUIBase";
import { NextBase } from "../../../NextBase";
export default class Form extends FormHandle{
    //uiBase=new NextUIBase();
    util=new NextBase();
    inputComponents:Array<string>=this.inputComponents.concat(['TextInput','CheckBoxInput','CurrentInput','ElectrodeSelect','PinSelect','TimeInput']);
    async onsubmit(event:any){
        event.preventDefault();
        if(this.state.id!=""){
            if(this.props.put){
                const request=await this.util.fetchClient(this.props.put+this.state.id,'PUT',this.state.record);
                const status=request.status;
                const dataResp=request.json;
                this.statusResponse=status;
                this.submissionResponse=dataResp;
                this.setState({...this.state,statusResponse:status,submissionResponse:dataResp});
            }
        }else
        {
            if(this.props.post){
                const request=await this.util.fetchClient(this.props.post,'POST',this.state.record);
                const status=request.status;
                const dataResp=request.json;
                this.statusResponse=status;
                this.submissionResponse=dataResp;
                this.setState({...this.state,statusResponse:status,submissionResponse:dataResp});
            }
        }
        if(this.props.onSubmit){
            this.props.onSubmit();
        }

    }
}