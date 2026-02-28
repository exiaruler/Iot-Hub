import { ReactNode } from "react";

interface Props{
    padding?:any;
    boarder?:any;
    children?:any;
}
export default function Group(props:any){
    var backgroundCol='';
    if(props.backgroundColor){
        backgroundCol=props.backgroundColor;
    }
    return(
        <div style={{padding:'4px',border:'1px solid #E0E0E0',margin:'4px',backgroundColor:backgroundCol}}>
        {
            props.children
        }
        </div>
    );
}