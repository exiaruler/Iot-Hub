import { Button, ButtonGroup } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
//import BackLogo from "../../assets/icons8-back-arrow-48.png";
import BackLogo from "../assets/icons8-back-arrow-48.png";
import Image from "next/image";
export default function BackButton(props:any){
    var caption="Back";
    var url="/";
    var width=40;
    var height=40;
    const nav=useNavigate();
    const setBack=()=>{
        if(props){
            if(props.caption){
                caption=props.caption;
            }
            if(props.url){
                url=props.url;
            }
        }
    }
    setBack();
    let goBackto=()=>{
        nav(url);
    }
    if(props.onClick){
        goBackto=props.onClick;
    }
        return(
            <div>
            <br/>
            <ButtonGroup className="Button-Regular">
            <Button variant="light" size="lg" onClick={goBackto} >
            <Image src={BackLogo} alt="Back" width={width} height={height}/>
            </Button>
            </ButtonGroup>
            </div>
            
            
        );
}