'use client'
import TabComponent from "@/components/Tab/TabComponent";
import TabGroup from "@/components/Tab/TabGroup";
import TableComponentColumn from "@/components/Table/TableComponentColumn";
import { Col, Row, Stack } from "react-bootstrap";
import TableComponent from "../next-components/TableComponent";
import { useRef, useState } from "react";
import SelectInput from "../next-components/input/SelectInput";
import FormHandle from "@/components/form/FormHandle";
import SaveButton from "../next-components/buttons/SaveButton";
import { RegularButton } from "../next-components/buttons/RegularButton";
import { NextBase } from "@/NextBase";
import NewButton from "../next-components/form/NewButton";
import Form from "../next-components/form/Form";
import TextInput from "../next-components/input/TextInput";
import CheckBoxInput from "../next-components/input/CheckBoxInput";
import TimeInput from "../next-components/input/TimeInput";
import Content, { ObjectArray, ObjectRecord } from "../next-components/layout/Content";
interface Props{
    form:Object;
    schedule:ObjectArray;
    devices:ObjectArray;
}
export default function Client({form,schedule,devices}:Props){
    const base=new NextBase();
    const [modeSelectView,setModeSelectView]=useState(true);
    const [devicesList,setDeviceList]=useState(devices);
    const [scheduleList,setScheduleList]=useState(schedule);
    const [deleteBtn,setDeleteBtn]=useState(true);
    const [functions,setFunctions]=useState([]);
    const [modes,setModes]=useState([]);
    const tabRef=useRef<TabGroup|null>(null);
    const formRef=useRef<FormHandle|any>(null);
    const tableRef=useRef<TableComponent|null>(null);
    const deviceRef=useRef<SelectInput>(null);
    const functionRef=useRef<SelectInput>(null);
    const startUpCheckRef=useRef<CheckBoxInput>(null);
    const routineCheckRef=useRef<CheckBoxInput>(null);
    const [routineHidden,setRoutineHidden]=useState({
        startup:false,
        routine:false
    })
    const [scheduleType,setScheduleType]=useState("");

    const submit=()=>{
        let form=formRef.current;
        if(form.statusResponse==200){
            location.reload();
        }
    }
    const backSchedules=()=>{
        tabRef.current?.handleTabSwitch('schedule');
    }
    const deviceSelectChange=(value:number)=>{
        let dev=deviceRef.current;
        if(dev!=null){
            let device=dev.getObjectValueKey(value);
            if(device!=null){
                setFunctions(device.routes);
            }else {
                setFunctions([]);
            }
        }
    }
    const functionSelectChange=(value:number)=>{
        let fun=functionRef.current;
        let functionF=fun?.getObjectValueKey(value);
        if(functionF!=null){
            if(functionF.mode.length>0){
                setModeSelectView(false);
                setModes(functionF.mode);
            }else
            {
                setModeSelectView(false);
                setModes([]);
            }
        }else{
            setModeSelectView(false);
            setModes([]);
        }
    }
    const routineChangeHandler=()=>{
        const startup=startUpCheckRef.current;
        const routine=routineCheckRef.current;
        if(startup?.value) {
            setRoutineHidden({...routineHidden,routine:true,startup:false});
        }else if(routine?.value) {
            setRoutineHidden({...routineHidden,startup:true,routine:false});
        }else if(!startup?.value){
            setRoutineHidden({...routineHidden,startup:false,routine:false});
        }else if(!routine?.value) setRoutineHidden({...routineHidden,startup:false,routine:false});
    }   
    const newRecord=()=>{
        setRoutineHidden({...routineHidden,routine:false,startup:false});
        setFunctions([]);
        setModes([]);
        setModeSelectView(true);
        setDeleteBtn(true);
        const table=tableRef.current;
        table?.clearRowSelect();
    }
    const deleteRecord=async()=>{
        let form=formRef.current;
        let id=form?.getId();
        if(id>0){
            const request=await base.fetchClientQuery('/schedule/delete-schedule/'+id,'DELETE',null);
            if(request.status==200){
                location.reload();
            }
        }

    }
    const selectRecord=(record:Record<string,any>)=>{
        let table=tableRef.current;
        let form=formRef.current;
        if(!table?.sameRow){
            setRoutineHidden({...routineHidden,routine:false,startup:false});
            form?.setRecord(record);
            let functions=devices.find((rec:ObjectRecord)=>rec?.id==record.deviceId)?.routes||[];
            setFunctions(functions);
            if(record.startup)setRoutineHidden({...routineHidden,routine:true,startup:false});
            if(record.repeatTask)setRoutineHidden({...routineHidden,startup:true,routine:false});
            for(let i=0; i<functions.length; i++){
                let modes=functions[i].mode;
                let found=false;
                let findMode=modes.find((rec:any)=>rec.id==record.modeId)||null;
                if(findMode!=null){
                    setModeSelectView(false);
                    setModes(modes);
                    found=true;
                    setDeleteBtn(false);
                    break;
                }
            }
        }else
        {
            form?.newRecord();
            setRoutineHidden({...routineHidden,routine:false,startup:false});
            setFunctions([]);
            setModeSelectView(true);
            setModes([]);
            setDeleteBtn(true);
        }
    }
    const randomiseChange=(event:React.ChangeEvent<HTMLInputElement>)=>{
        let value=event.target.checked;
        if(functions.length>0) setModeSelectView(value);
    }
    const onChangeTaskType=(name:string,value:boolean)=>{
        formRef.current?.onChangeRecord(name,value);
    }
    return(
        <div>
        <Content>
        <Row>
        <Col md={2}></Col>
        <Col md={9}>
        <TabGroup defaultActiveKey={"schedule"} ref={tabRef}>
        <TabComponent title={"Schedule Functions"} eventKey={"schedule"}>
        <TableComponent ref={tableRef} results={scheduleList as Array<Record<string, any>>} idKey={"id"} rowSelect={true} onClick={selectRecord}>
        <TableComponentColumn key={"name"} columnName={"Name"} />
        <TableComponentColumn key={"startup"} columnName={"Startup"} size={10}/>
        <TableComponentColumn key={"repeatTask"} columnName={"Automated"} size={10}/>
        <TableComponentColumn key={"status"} columnName={"Status"} size={10}/>
        </TableComponent> 
        </TabComponent>
        <TabComponent title={"Schedule"} eventKey={"form"}>
        <Row>
        <Col md={5}>
        <Form  post={"/schedule/add-schedule-socket"} put={"/schedule/update-schedule/"} recordLayout={form} ref={formRef} onSubmit={submit} idKey={"id"}>
        <TextInput formRef={formRef} label={"Name"} type={"text"} rows={0} name={"name"} required={true}/>
        <SelectInput ref={deviceRef} formRef={formRef} name={"deviceId"} size={undefined} api={""} label="Device" valueKey={"id"} displayKey={"name"} options={devicesList} onChange={(event: any) => deviceSelectChange(event.target.value)} type={""} rows={0}/>
        <SelectInput ref={functionRef} formRef={formRef} name={"routeId"} warning={""} size={undefined} api={""} label="Function" valueKey={"id"} displayKey={"route"} options={functions} onChange={(event: any) => functionSelectChange(event.target.value)} type={""} rows={0}/>
        <CheckBoxInput formRef={formRef} name={"modeRandom"} label={"Mode Randomise"} type={""} size={undefined} onChange={(event: any) => randomiseChange(event)} rows={0}/>
        {!modeSelectView?
        <SelectInput formRef={formRef} name={"modeId"} warning={""} size={undefined} api={""} label="Mode" valueKey={"id"} displayKey={"mode"} options={modes} type={""} rows={0}/>
        :null}
        <Stack direction="horizontal" gap={2}>
        <CheckBoxInput ref={startUpCheckRef} hidden={routineHidden.startup} formRef={formRef} type={""} size={undefined} label={"Startup"} name={"startup"} rows={0} onChange={routineChangeHandler}/>
        <CheckBoxInput ref={routineCheckRef} hidden={routineHidden.routine} formRef={formRef} type={""} size={undefined} label={"Routine"} name={"repeatTask"} rows={0} onChange={routineChangeHandler}/>
        </Stack>
        {routineHidden.startup?
        <TimeInput name="time" formRef={formRef} seconds={true} mins={true} hour={true} label={"Time Set At"} type={""} rows={0}/>
        :null}
        <CheckBoxInput formRef={formRef} name={"status"} label={"Status"} size={undefined} type={""} rows={0}/>
        <RegularButton caption={"Back"} size={undefined} type={"button"} onClick={backSchedules}/>
        <RegularButton caption={"Delete"} size={undefined} type={"button"} disabled={deleteBtn} onClick={deleteRecord}/>
        <NewButton caption={""} size={undefined} type={undefined} formRef={formRef} onClick={newRecord}/>
        <SaveButton caption={"Save"} size={undefined} type={undefined}/>
        </Form>
        </Col>
        </Row>
        </TabComponent>
        </TabGroup>
        </Col>
        <Col md={4}></Col>
        </Row>
        </Content>
        </div>
    )
}