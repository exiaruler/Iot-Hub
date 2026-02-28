'use client'

import SubForm from "./SubForm";

export default class ModeSubForm extends SubForm{
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
                const index=this.props.index;
                const form=this.props.formRef.current;
                if(form){
                    let arr=form.getRecordValue(this.props.objectKey)||[];
                    if(arr.hasOwnProperty(index!)){
                        // modify boardAction except pins
                        arr[index!].mode=this.record?.mode;
                        const boardAct=this.record?.boardAction;
                        const keys=Object.keys(boardAct||{});
                        keys.forEach((key:string)=>{
                            if(key!=='pins'){
                                arr[index!].boardAction[key]=boardAct![key];
                            }
                        });
                        form.onChangeRecord(this.props.objectKey,arr);
                    }else{
                        arr.push(this.record);
                        form.onChangeRecord(this.props.objectKey,arr);
                    }
                }
            }
        }
    }
}
