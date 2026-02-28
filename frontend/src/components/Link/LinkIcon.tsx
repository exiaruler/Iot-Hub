import { ReactNode } from "react";

interface Props{
    children?:ReactNode;
}
export default function LinkIcon(props:Props){
    return(
        <div className="Link-icon">
        {
            props.children
        }
        </div>
    )
}