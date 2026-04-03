'use client'
import FormHandle, { Props, RecordContext } from "@/components/form/FormHandle";
import { NextBase } from "../../../NextBase";
interface PropsMod extends Props{
    putUrlArgument?:string;
    objectKey?:string;
    formRef?:FormHandle|any;
    array?:boolean;
    index?:number|-1;
}
export default class Form extends FormHandle{
    util=new NextBase();
    declare props:PropsMod;
    
    public formOnChange():void{
        if(this.props.formRef){
            // for single object on that key
            if(this.props.objectKey&&!this.props.array){
                const form=this.props.formRef.current;
                if(form){
                    form.onChangeRecord(this.props.objectKey,this.record);
                }
            // if array on that key is used
            }
            if(this.props.array&&this.props.objectKey){
               let index=this.props.index;
                const form=this.props.formRef.current;
                if(form){
                    let arr=form.getRecordValue(this.props.objectKey)||[];
                    if(arr.hasOwnProperty(index!)){
                        arr[index!].boardAction.pins=this.record?.boardAction.pins;
                        form.onChangeRecord(this.props.objectKey,arr);
                    }else{
                        arr.push(this.record);
                        form.onChangeRecord(this.props.objectKey,arr);
                    }
                }
            }
        }
    }

    public async onsubmit(event:React.FormEvent<HTMLFormElement>):Promise<void>{
        event.preventDefault();
        if(this.props.debug) debugger;
        if(this.state.id!=""){
            if(this.props.put){
                let secondArg="";
                if(this.props.putUrlArgument) secondArg=this.props.putUrlArgument;
                const request=await this.util.fetchClientQuery(this.props.put+this.state.id+secondArg,'PUT',this.state.record);
                this.response=request;
                if(request!=null&&!this.props.streamOverride){
                    const status=await request.status;
                    const ok=await request.ok;
                    this.ok=ok
                    const dataResp=await request.json;
                    this.statusResponse=status;
                    this.submissionResponse=dataResp;
                    this.setState({...this.state,statusResponse:status,submissionResponse:dataResp});
                }else if(!this.props.streamOverride) this.setState({...this.state,statusResponse:400,submissionResponse:null});
            }
        }else
        {
            if(this.props.post){
                const request=await this.util.fetchClientQuery(this.props.post,'POST',this.state.record);
                this.response=request;
                if(request!=null&&!this.props.streamOverride){
                    const status=await request.status;
                    this.ok=request.ok;
                    const dataResp=await request.json;
                    this.statusResponse=status;
                    this.submissionResponse=dataResp;
                    this.setState({...this.state,statusResponse:status,submissionResponse:dataResp});
                }else if(!this.props.streamOverride) this.setState({...this.state,statusResponse:400,submissionResponse:null});
            }
        }
        if(this.props.onSubmit){
            this.props.onSubmit(event);
        }
    }
    render(){
        return(
            <form onSubmit={(event:React.FormEvent<HTMLFormElement>)=>this.onsubmit(event)} >
            <RecordContext.Provider value={this.state.record}>
            {
                this.props.children
            }
            </RecordContext.Provider>
            </form>
            
        )
    }
    
}