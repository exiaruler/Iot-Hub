import { Component, useEffect, useState } from 'react';
import { CardText, Col, Form, Row, Stack } from 'react-bootstrap';
import Card from 'react-bootstrap/Card';
import gitlogo from './assets/github-mark.png';
import { Link,useNavigate } from 'react-router-dom';
import UiBase from '../../base/UiBase';
//import props from './../../base/interfaces/project';
import { ButtonComponent } from '../../component-base/ButtonComponent';
import ProjectAPI from '../../api/ProjectsAPI';
import Image from 'next/image';
export default function ProjectCard(props:any){
  const id=props.id;
  var disableStyle='';
  const base=new UiBase();
  const login=props.login;
  const navigate = useNavigate();
  const project=new ProjectAPI();
  const [url,setUrl]=useState(false);
  const showGitLogo=(url:String)=>{
    if(url!==""){
      setUrl(true);
    }else setUrl(false);
  }
  const disableLink=()=>{
    const disable='disabled-link';
    if(id===0){
      disableStyle=disable;
    }
  }
  const deleteProject=async(obj: any)=>{
    const id=obj.id;
    const del=await project.deleteProject(id);
    if(del?.status==200){
      navigate(0);
    }
  }
  const repositoryHandle=async()=>{
    const request=await project.repositoryCount(id);
  }
  useEffect(()=>{
    showGitLogo(props.url);
    disableLink();
  });
  return (
    <div>
    <Row>
    <Col>
    <Card style={{minHeight:'11rem',backgroundColor:'	#FFFAFA'}}>
    <Card.Body>
    <Link to={'/project/'+id} className={disableStyle} state={{record:props}}>
    <Card.Title as="h4">{props.name}</Card.Title>
    <CardText>{base.util.cutOffString(props.description,100," ....")}</CardText>
    </Link>
    <div>
    {
      url?
      <Card.Link onClick={repositoryHandle} href={props.url}><Image src={gitlogo} height={30} width={30} alt="Repistory"/></Card.Link>
      :null
    }
    {
      login?
      <div className='inlineblock'>
      <Form.Label className='inlineblock'>Views</Form.Label>
      <p className='inlineblock'>{props.views}</p>
      <Form.Label className='inlineblock'>GitHub Clicks:</Form.Label>
      <p className='inlineblock'>{props.repositoryClicks}</p>
      </div>
      :null
    }
    </div>
    <div>
    {login?
    <>
    
    <Stack direction="horizontal" gap={1}>
    <Link to={"/form/project/"+id} state={{record:props.project}}><ButtonComponent id={""} caption={'Update'} variant={''} onClick={undefined} size={''} active={false} disabled={false} type={undefined} /></Link>
  
    <Link to={"/project/view-statistics/"+id} state={{record:props.project}}><ButtonComponent id={""} caption={'Statistcs'} variant={''} onClick={undefined} size={''} active={false} disabled={false} type={undefined} /></Link>
   
    <Link to={""} state={{login:login}} onClick={()=>deleteProject({id})}><ButtonComponent id={""} caption={'Delete'} variant={'danger'} onClick={undefined} size={''} active={false} disabled={false} type={undefined} /></Link>
    </Stack>
    
    </>
    :null}
    </div>
    
    </Card.Body>
    
    </Card>
    </Col>
    </Row>
    </div>);
}
