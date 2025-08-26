'use client'
import GridCard from "@/components/GridCard";
import { Col, Row } from "react-bootstrap";
import ModalBox from "@/components/modal/ModalBox";
import { NextUIBase } from "@/NextUIBase";
import { useEffect, useRef, useState } from "react";
import { RegularButton } from "../next-components/buttons/RegularButton";
import { FormGenText } from "@/components/formGenComponents/FormGenText";
import { useRouter } from 'next/navigation';
import FormGenSelect from "@/components/formGenComponents/FormGenSelect";
interface Props{
    boards:Array<Object>;
    addForm:Object;
}
// user boards page
export default function Client(props:any){
    const router = useRouter();
    const modalRef:any=useRef(null);
    const gridRef:any=useRef(null);
    const uiBase=new NextUIBase();
    const [form,setForm]=useState({
        name:"",
        password:"",
        hardwareModel:null
    });
    const [warning,setWarning]=useState({
        name:'',
        password:'',
        hardwareModel:''
    });
    const [boards,setBoards]:any=useState([]);
   
    const submit=async()=>{
        console.log(form);
        var input=form;
        input.hardwareModel=JSON.parse(form.hardwareModel);
        const postForm=await uiBase.util.fetchClient('/board/board','POST',form);
        const status=postForm.status
        if(postForm.ok&&status===200){
            var addedBoard=postForm.json;
            /*
            if(addedBoard!=null){
                setBoards(boards.push(addedBoard));
                modalRef.close();
            }
            */
            window.location.reload();
        }else console.log(postForm?.json());
    }

    const addHandle=(event:any)=>{
        var record=gridRef.current.getSelectedCard();
        if(record.id==0){
            modalRef.current.open();
        }else
        {
            router.push('/boards/'+record.boardId);
        }
        console.log(record);
    }
    const close=()=>{
        clear();
    }
    const clear=()=>{
        setForm({name:"",
        password:"",
        hardwareModel:null});
        setWarning({
        name:'',
        password:'',
        hardwareModel:''
        });
    }
    const loadBoards=()=>{
        setBoards(props.data.boards);
    }
    
    useEffect(()=>{
        loadBoards();
    },[])
    return(
        <div>
        <Row>
        <Col md={2} xs={2}></Col>
        <Col md={10} xs={14}>
        <div>
        <GridCard ref={gridRef} mdCol={3} smCol={2} valueKey={"id"} data={boards} titleKey={"name"} bodyKey={""} onClick={addHandle} />
        </div>
        </Col>
        <Col>
        </Col>   
        </Row>
        <ModalBox ref={modalRef} title={"Add Board"} submitCaption="Add" submit={submit} onClose={close}>
        <div>
        <Row>
        <Col md={7} xs={9}>
        <FormGenText label={"Name"} type={""} rows={0} value={form.name} warning={warning.name} onChange={(event:any)=>uiBase.onChange('name',event.target.value,setForm,form)}/>
        <FormGenText label={"Password"} type={"password"} rows={0} warning={warning.password} value={form.password} onChange={(event:any)=>uiBase.onChange('password',event.target.value,setForm,form)}/>
        <FormGenSelect label={"Board Model"} valueKey={""} warning={warning.hardwareModel} displayKey={"boardName"} onChange={(event:any)=>uiBase.onChange('hardwareModel',event.target.value,setForm,form)} value={form.hardwareModel} options={props.data.hardwares}/>
        </Col>
        </Row>
        </div>  
        </ModalBox>
        </div>
    );
}