'use client'
import { NextUIBase } from "@/NextUIBase";
import { ReactNode } from "react";
interface Props{
    showElement?:boolean;
    children?:ReactNode;
}
// show component only in development mode
export default function Dev({ showElement, children }:Props){
    const base=new NextUIBase

    const show=():boolean=>{
        let show=false;
        if(base.util.baseUrlIo!="http://localhost:8080"){
            show=true;
        }
        return show;
    }
    return(
        <div>
        {!show()?
            children
        :null}
        </div>
    )
}