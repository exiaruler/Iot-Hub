'use client'

import SubForm, { PropsMod } from "./SubForm";

export default class PinSubForm extends SubForm{
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
}