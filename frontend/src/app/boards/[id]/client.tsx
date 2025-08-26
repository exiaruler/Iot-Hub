'use client'
import ModalBox from "@/components/modal/ModalBox"
import { RegularButton } from "@/app/next-components/buttons/RegularButton"
import { FormGenText } from "@/components/formGenComponents/FormGenText"
import TabComponent from "@/components/Tab/TabComponent"
import TabGroup from "@/components/Tab/TabGroup"
import { Row, Col } from "react-bootstrap"
import { useEffect, useRef, useState } from "react"
import DeleteBox from "@/components/modal/DeleteBox"
import { Board } from "@/interface/boardInterface"
import { Hardware } from "@/interface/hardwareInterface"
import { NextUIBase } from "@/NextUIBase"
import FormComponent from "@/components/formGenComponents/layout/formComponent"
import FormLayout from "@/components/formGenComponents/layout/formComponent"
import { useRouter } from 'next/navigation';
import TableComponent from "@/app/next-components/TableComponent"
import TableComponentColumn from "@/components/Table/TableComponentColumn"
import TableComponentClass from "@/components/Table/TableComponentClass"
import DeleteButton from "@/app/next-components/buttons/DeleteButton"
// board page
export default function Client(props:any){
    const passwordModalRef:any=useRef(null);
    const deleteModalRef:any=useRef(null);
    const functionRef:any=useRef(null);
    const tabGrpRef:any=useRef(null);
    const functionTblRef:any=useRef(null);
    const [modeTabShow,setTabShow]=useState(true);
    const formRefs:any=useRef([]);
    const [activated,setActivated]=useState(true);
    const [board,setBoard]:Board=useState(null);
    const [devices,setDevices]:any=useState([]);
    const [device,setDevice]:any=useState(null);
    const [hardware,setHardware]:Hardware=useState(null);
    const router = useRouter();
    const uiBase=new NextUIBase();
    const openChangePass=()=>{
        passwordModalRef.current.open();
    }
    const openDelete=()=>{
        deleteModalRef.current.open();
    }
    const loadBoard=(board:Board)=>{
        setActivated(board.activated);
        setBoard(board);
        if(board.device.length>0){
            setDevices(board.device);
        }
    }
    const loadHardware=(hardware:Hardware)=>{
        setHardware(hardware);
    }
    const loadForms=(forms:any)=>{
        formRefs.current.push(forms);
    }
    const handleTabSelect=(key:any)=>{
        if(key!=="board"&&key!=="add"){
            var index=parseInt(key);
            setDevice(devices[index]);
        }
    }
    const status=(bool:boolean)=>{
        var show="Inactive";
        if(!bool) show="Active";
        return show;
    }
    const openFunction=()=>{
        var id=0;
        const funcRec=functionTblRef.current.state.selectRowRec;
        if(funcRec!=null){
            id=funcRec.id;
        }
        router.push('/device/function/'+board.boardId+'/'+device.deviceId+'/'+id);
    }
    const deletehandle=()=>{
        router.push('/boards');
    }
    useEffect(()=>{
        loadBoard(props.data.board.board);
        loadHardware(props.data.board.hardware);
        loadForms(props.data.deviceForm);
        console.log(props.data);
    },[])
    return(
        <div>
        <Row>
        <Col md={2} xs={2}></Col>
        <Col md={10} xs={14}>
        <TabGroup defaultActiveKey={"board"} ref={tabGrpRef} onSelect={handleTabSelect}>
        <TabComponent title={board?.name} eventKey={"board"}>
        <div>
        <Row>
        <Col md={3} xs={9}>
        <FormGenText label={"Board ID"} type={""} rows={0} value={board?.boardId} readOnly={true}/>
        <FormGenText label={"Board Model"} type={""} rows={0} value={hardware?.boardName} readOnly={true}/>
        <FormGenText label={"Status"} type={""} rows={0} value={status(board?.activated)} readOnly={true}/>
        </Col>
        <Col md={3} xs={9}>
        <FormGenText label={"RAM Usage"} type={""} rows={0} value={board?.ramUsage} readOnly={true}/>
        <FormGenText label={"Total RAM"} type={""} rows={0} value={hardware?.maxRam} readOnly={true}/>
        <FormGenText label={"Local IP"} type={""} rows={0} value={board?.ip} readOnly={true}/>
        </Col>
        <Col md={3} xs={9}>
        <FormGenText label={"Routine Check"} type={""} rows={0} value={board?.periodicCheck} readOnly={true}/>
        <FormGenText label={"Last Connection Date"} type={""} rows={0} value={board?.lastConnectDate} readOnly={true}/>
        <FormGenText label={"Last Connection Time"} type={""} rows={0} value={board?.lastConnectTime} readOnly={true}/>
        </Col>
        </Row>
        <Row>
        <Col>
        <RegularButton caption={"Change Password"} onClick={openChangePass} size={undefined} type={undefined}/>
        <RegularButton caption={"Configurations"} size={undefined} type={undefined}/>
        <RegularButton disabled={!activated} caption={"Reset"} size={undefined} type={undefined}/>
        <RegularButton disabled={!activated} caption={"Reset Board Configuration"} size={undefined} type={undefined}/>
        <RegularButton caption={"Delete Board"} size={undefined} type={undefined} onClick={openDelete}/>
        </Col>    
        </Row>
        </div>
        </TabComponent>
        {
            devices.map((dev:any,key:number)=>(
                <TabComponent title={dev.name} eventKey={key} disabled={!activated}>
                <TabGroup defaultActiveKey={"overview"}>
                <TabComponent title={"Overview"} eventKey={"overview"}>
                <Row>
                <Col>
                <RegularButton caption={"Update Device"} size={undefined} type={undefined}/>
                <RegularButton caption={"Delete Device"} size={undefined} type={undefined}/>
                </Col>
                </Row>
                </TabComponent>
                <TabComponent title={"Functions"} eventKey={"functions"}>
                <Row>
                <Col>
                <TableComponent ref={functionTblRef} results={dev?.routes} idKey={"id"} rowSelect={true} onDoubleClick={openFunction}>
                <TableComponentColumn key={"route"} columnName={"Function"} />
                <TableComponentColumn columnName={"Startup"} key={""} size={5}/>
                <TableComponentColumn key={"modes"} columnName={"Modes"} size={5}/>
                </TableComponent>
                </Col>
                </Row>
                <Row>
                <Col>
                <RegularButton caption={"Add Function"} size={undefined} type={undefined} onClick={openFunction}/>
                <DeleteButton caption={"Delete Function"} size={undefined} type={undefined}/>
                </Col>
                </Row>
                </TabComponent>
                </TabGroup>
                </TabComponent>
            ))
        }
        <TabComponent title={"Add Device"} eventKey={"add"} disabled={!activated}>
        <FormLayout id={""} formId={""} valueKey={""} externalUrl={uiBase.util.baseUrlIo} form={props.data.deviceForm}/>
        </TabComponent>
        </TabGroup>
        </Col>
        </Row> 
        <ModalBox ref={passwordModalRef} title={"Password Change"}>
        <Row>
        <Col md={7}>
        <FormGenText label={"New Password"} type={"password"} rows={0} value={""}/>
        <FormGenText label={"Confirm New Password"} type={"password"} rows={0} value={""}/>
        </Col>
        </Row>
        </ModalBox>
        <ModalBox title={"Reset Board Configuration"}>
        <Row>
        <Col md={7}>
        <p>Resetting Board Configuration will remove board local WIFI authentication and board detail and deactivate board until reactivated</p>
        </Col>
        </Row>
        </ModalBox>
        <DeleteBox ref={deleteModalRef} title={"Delete Board"} deleteApi={"/board/"} baseUrl={uiBase.util.baseURL+'/api'} param={board?.id} afterSubmit={deletehandle}>
        <p>Are you sure you want to delete {board?.name}?</p>
        <p>All devices and functionality associated will be lost</p>
        </DeleteBox>
        </div>
    )
}