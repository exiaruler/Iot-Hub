'use client'
import TabComponent from "@/components/Tab/TabComponent";
import TabGroup from "@/components/Tab/TabGroup";
import TableComponentColumn from "@/components/Table/TableComponentColumn";
import { Col, Row, Stack } from "react-bootstrap";
import TableComponent from "../next-components/TableComponent";
import { FormGenCheckBox } from "@/components/formGenComponents/FormGenCheckBox";
import { useRef, useState } from "react";
import FormGenSelect from "@/components/formGenComponents/FormGenSelect";
import FormHandle from "@/components/form/FormHandle";
import SaveButton from "../next-components/buttons/SaveButton";
import { RegularButton } from "../next-components/buttons/RegularButton";
import { NextUIBase } from "@/NextUIBase";
import { NextBase } from "@/NextBase";
import NewButton from "../next-components/form/NewButton";
import Form from "../next-components/form/Form";
import TextInput from "../next-components/input/TextInput";
import CheckBoxInput from "../next-components/input/CheckBoxInput";
interface Props{
    form:Object;
    schedule:Array<Object>;
    devices:Object;
}
export default function Client({form,schedule,devices}:Props){
    const base=new NextBase();
    const [modeSelectView,setModeSelectView]=useState(false);
    const [devicesList,setDeviceList]=useState(devices);
    const [scheduleList,setScheduleList]=useState(schedule);
    const [deleteBtn,setDeleteBtn]=useState(true);
    const [functions,setFunctions]=useState([]);
    const [modes,setModes]=useState([]);
    const tabRef=useRef<TabGroup|null>(null);
    const formRef=useRef<FormHandle|null>(null);
    const tableRef=useRef<TableComponent|null>(null);
    const deviceRef=useRef(null);
    const functionRef=useRef(null);
    const [scheduleType,setScheduleType]=useState("");

    const submit=()=>{
        var form=formRef.current;
        if(form?.statusResponse==200){
            location.reload();
        }
    }
    const backSchedules=()=>{
        tabRef.current.handleTabSwitch('schedule');
    }
    const deviceSelectChange=(value:number)=>{
        var dev=deviceRef.current;
        var device=dev.getObjectValueKey(value);
        if(device!=null){
            setFunctions(device.routes);
        }else {
            setFunctions([]);
        }
    }
    const functionSelectChange=(value:number)=>{
        var fun=functionRef.current;
        var functionF=fun.getObjectValueKey(value);
        if(functionF!=null){
            if(functionF.mode.length>0){
                setModeSelectView(true);
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
    const newRecord=()=>{
        setFunctions([]);
        setModes([]);
        setModeSelectView(false);
        setDeleteBtn(true);
        const table=tableRef.current;
        table?.clearRowSelect();
    }
    const deleteRecord=async()=>{
        var form=formRef.current;
        var id=form?.getId();
        if(id>0){
            debugger;
            const request=await base.fetchClient('/schedule/'+id,'DELETE',null);
            if(request.status==200){
                location.reload();
            }
        }

    }
    const selectRecord=(record:Object)=>{
        var table=tableRef.current;
        var form=formRef.current;
        if(!table?.sameRow){
            form?.setRecord(record);
            var functions=devices.find((rec:any)=>rec.id==record.deviceId).routes||[];
            setFunctions(functions);
            for(var i=0; i<functions.length; i++){
                var modes=functions[i].mode;
                var found=false;
                var findMode=modes.find((rec:any)=>rec.id==record.modeId)||null;
                if(findMode!=null){
                    setModeSelectView(true);
                    setModes(modes);
                    found=true;
                    setDeleteBtn(false);
                    break;
                }
            }
        }else
        {
            form?.newRecord();
            setFunctions([]);
            setModeSelectView(false);
            setModes([]);
            setDeleteBtn(true);
        }
    }
    const onChangeTaskType=(name:string,value:boolean)=>{
        formRef.current?.onChangeRecord(name,value);
    }
    console.log(devices)
    return(
        <div>
        <Row>
        <Col md={2}></Col>
        <Col md={9}>
        <TabGroup defaultActiveKey={"schedule"} ref={tabRef}>
        <TabComponent title={"Schedule Functions"} eventKey={"schedule"}>
        <TableComponent ref={tableRef} results={scheduleList} idKey={"id"} rowSelect={true} onClick={selectRecord}>
        <TableComponentColumn key={"name"} columnName={"Name"} />
        <TableComponentColumn key={"startup"} columnName={"Startup"} size={10}/>
        <TableComponentColumn key={"repeatTask"} columnName={"Automated"} size={10}/>
        <TableComponentColumn key={"status"} columnName={"Status"} size={10}/>
        </TableComponent> 
        </TabComponent>
        <TabComponent title={"Schedule"} eventKey={"form"}>
        <Row>
        <Col md={5}>
        <Form post={"/schedule/schedule"} put={"/schedule/"} recordLayout={form} ref={formRef} onSubmit={submit} idKey={"id"}>
        <TextInput formRef={formRef} label={"Name"} type={"string"} rows={0} name={"name"} value={""}/>
        <FormGenSelect ref={deviceRef} formRef={formRef} name={"deviceId"} warning={""}  size={undefined} api={""} label="Device" valueKey={"id"} displayKey={"name"} options={devicesList} onChange={(event:any)=>deviceSelectChange(event.target.value)}/>
        <FormGenSelect ref={functionRef} formRef={formRef} name={"routeId"} warning={""}  size={undefined} api={""} label="Function" valueKey={"id"} displayKey={"route"} options={functions} onChange={(event:any)=>functionSelectChange(event.target.value)}/>
        {modeSelectView?
        <FormGenSelect formRef={formRef}  name={"modeId"} warning={""} value={""} size={undefined} api={""} label="Mode" valueKey={"id"} displayKey={"mode"} options={modes}/>
        :null}
        <CheckBoxInput formRef={formRef} type={""} size={undefined} label={"Startup"} name={"startup"}   />
        <CheckBoxInput formRef={formRef} type={""} size={undefined} label={"Routine"} name={"repeatTask"} />
        <Stack direction="horizontal" gap={2}>
        
        </Stack>
        <CheckBoxInput formRef={formRef} name={"status"} label={"Status"} type={""} size={undefined} value={false}/>
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
        </div>
    )
}