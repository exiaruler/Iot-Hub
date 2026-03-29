'use client'
import { Component, RefObject } from "react";
import UiBase from "./base/UiBase";
import { NextBase } from "./NextBase";
import { useSelector } from "react-redux";
import { getPages } from "./redux/slice/pageSlice";

// base for UI
export class NextUIBase extends UiBase{
    public util=new NextBase();

    public generateKeyClient(){
        var key=this.util.generateEncryptKey();
        sessionStorage.setItem(this.util.originUrl+"-en",key);
    }
    public checkKey(){
        var key=sessionStorage.getItem(this.util.originUrl+"-en");
        return key;
    }
    public addInputRefComponent(array:RefObject<Array<any>>,component:Component|null){
            const exists=array.current.find((ele:any)=>component==ele)||null;
            if(exists==null&&component!=null)array.current.push(component);
            return array;
        }
    
    public forceUpdateRefComponents(array:RefObject<Array<any>>){
            array.current.map((ele:Component)=>{
                const comp=ele;
                comp?.forceUpdate();
            });
    }
       
}
