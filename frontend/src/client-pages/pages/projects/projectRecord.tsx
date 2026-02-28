import { useEffect, useState } from "react";
import { Button, ButtonGroup, Col, Container, Nav, Row } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import ProjectAPI from "../../api/ProjectsAPI";
import GitHubLogo from "../../assets/github-mark.png";
import BackButton from "../../components/Buttons/BackButton";
import ParagraphBody from "../../components/ParagraphBody";
import Group from "../../components/Group";
import Toolbar from "../../components/Toolbar/Toolbar";
import LinkIcon from "../../components/Link/LinkIcon";
export default function ProjectRecord(props:any){
    const {id}=useParams();
    const api=new ProjectAPI();
    const [load,setLoad]=useState(false);
    const [project,setProject]=useState({
        name:"",
        description:"",
        url:""
    });
    const [showLinks,setLinks]=useState(false);
    const getProject=async ()=>{
        if(props.record==null){
            try{
                const project=await api.getProject(id);
                setProject(project);
                setLoad(true);
                displayLinks(project.url);
                api.viewCount(id);
            }catch(err){
                
            } 
        }else{
            setProject(props.record);
            setLoad(true);
            displayLinks(props.record.url);
            api.viewCount(id);
        }
    }
    const displayLinks=(url:string)=>{
        if(url!==""){
            setLinks(true);
        }
    }
    const repositoryHandle=async()=>{
        const request=await api.repositoryCount(id);
    }
    useEffect(() => {
        getProject();
    },[]);
    
    return(
        <div>
        <Group>
        <Row>
        <Col id="BackSection" xs={5} md={2}>
        <div className="">
        {load?
        <BackButton url={"/projects"}/>
        :null}
        </div>
        </Col>
        <Col xs={12} md={8}>
        <div className="ProjectTitle">
        <h1>{project.name}</h1>
        {showLinks?
        <Toolbar backgroundColor={'#F0FFFF'}>
        <LinkIcon>
        <a href={project.url} onClick={repositoryHandle}><img src={GitHubLogo} width={25} height={25} alt="GitRepo"/></a>
        </LinkIcon>
        </Toolbar>
        :null}
        </div>
        <ParagraphBody value={project.description}/>
        </Col>
        </Row>
        </Group>

        </div>
    );
}