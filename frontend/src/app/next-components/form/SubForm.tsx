'use client'
import FormHandle, { Props, RecordContext } from "@/components/form/FormHandle";
import Form from "./Form";
export interface PropsMod extends Props{
    objectKey?:string;
    formRef?:FormHandle|any;
    array?:boolean;
    index?:number|-1;
    id?:string;
    arrayKeyUpdate?:string|Array<string>;
}
export default class SubForm extends Form{
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
                        if(this.props.arrayKeyUpdate&&this.props.arrayKeyUpdate instanceof Array){
                            let val=null;
                            for(var i=0; i< this.props.arrayKeyUpdate.length; i++){
                                const key=this.props.arrayKeyUpdate[i];
                                if(i===0){
                                    val=this.record![key];
                                }else val=val![key];
                            }
                        }else if(this.props.arrayKeyUpdate&&typeof this.props.arrayKeyUpdate==='string'){
                            arr[index!][this.props.arrayKeyUpdate]=this.record![this.props.arrayKeyUpdate];
                        }else arr[index!]=this.record;
                        form.onChangeRecord(this.props.objectKey,arr);
                    }else{
                        arr.push(this.record);
                        form.onChangeRecord(this.props.objectKey,arr);
                    }
                }
            }
        }
    }
    render(){
        return(
            <div onChange={()=>this.formOnChange()} >
            <RecordContext.Provider value={this.state.record}>
            {
                this.props.children
            }
            </RecordContext.Provider>
            </div>
        )
    }
}