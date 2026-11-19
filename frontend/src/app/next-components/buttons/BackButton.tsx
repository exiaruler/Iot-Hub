import { useRouter } from "next/navigation";
import { RegularButton } from "./RegularButton";
interface Props{
    url:string;
}
export default function BackButton(props:Props){
    const router = useRouter();
    const handleClick=()=>{
        if(props.url){
            router.push(props.url);
        }else router.back();
    }
    return(
        <RegularButton caption={"Back"} size={undefined} type={undefined} onClick={handleClick}/>
    )
}