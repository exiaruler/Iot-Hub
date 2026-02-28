import { useParams } from "react-router";
import FormGenLibary from "../../components/formGenComponents/FormGenLibary";
import { Alert, Button, Col, Form, ProgressBar, Row, Spinner, Stack } from "react-bootstrap";
import React, { forwardRef, ReactNode, useEffect, useImperativeHandle, useRef, useState } from "react";
import { useNavigate,useLocation } from "react-router-dom";
import { Component } from "../../base/interfaces/component";
import BackButton from '../../components/Buttons/BackButton';
import { SaveButton } from "../../components/Buttons/SaveButton";
import { ButtonComponent } from "../../components/Buttons/ButtonComponent";
import { FormAPI } from "../../api/FormAPI";
import UiBase from "../../base/UiBase";
import Group from "../../components/Group";
import FormHandle from "../../components/form/FormHandle";
import { ErrorGenOutput } from "../../base/interfaces/Response/ErrorGen";
import { ResponseOutput } from "../../base/interfaces/Response/Response";
import FormLayout from "../../components/formGenComponents/layout/formComponent";
interface Props{
  form?:any;
  externalUrl?:string,
  record?:Record<string,any>|null;
  entry?:Boolean;
  clearHandle?:CallableFunction;
  submitHandle?:CallableFunction;
  clearAction?:Boolean;
  id:string;
  formId:string;
  valueKey:string;
  onClick?:CallableFunction;
}
const form=forwardRef (function FormPage(props:Props,ref){
  const { state } = useLocation();
  let formJson;
  let location = useLocation();
  const [formLay,setFormLay]=useState({
    urlLocation:"",
    formName:"",
    postapi:"",
    updateapi:"",
    deleteapi:"",
    headers:"",
    redirect:"",
    retrieveApi:"",
    settingOverride:false,
    form:{}
  });
  const currentForm=location.pathname;
  // form used in entry record page
  let entryUsed=false;
  let stateModel:any=useRef(null);
  let errorClearRef:any=useRef([]);
  const api=new FormAPI();
  const [formError,setFormError]=useState<any>({});
  const {formId}=useParams();
  let {id}=useParams();
  let record:any=useRef(null);
  let formUpdate=useRef(false);
  const [formArr,setFormArr]=useState<any[]>([]);
  const formCompRef=useRef<Array<any>>([]);
  const formHandleRef=useRef<FormHandle|any>(null);
  const [submitBtn,setSubmitBtn]=useState(false);
  const [load,setLoad]=useState(true);
  const [loadSate,setLoadState]=useState(0);
  const [error,setError]=useState(true);
  const [formErrorMsg,setFormErrorMsg]=useState<any>({
    hide:true,
    error:""
  });
  let keyArr:any=useRef([]);
  const nav=useNavigate();
  const base=new UiBase();
  const formLib= new FormGenLibary();

  // set prop from the guardwrapper if possible
  const setFormProp=()=>{
    errorClearRef.current=[];
    if(props.form){
      formJson=props.form;
      setLoadState(10);
      if(formJson){
        setFormLay({
          urlLocation:location.pathname,
          formName:formJson.name,
          postapi:formJson.postApi,
          updateapi:formJson.updateApi,
          deleteapi:formJson.deleteApi,
          headers:formJson.headers,
          redirect:formJson.redirect,
          retrieveApi:base.util.getApiUrl()+formJson.retrieveApi,
          settingOverride:formJson.setting,
          form:formJson.form
        });
        renderForm(formJson);
      }
    }else findForm();
    
  }


  const findForm=async ()=>{
    // find form inhouse first before api call
    if(props.form){
      setLoadState(10);
      renderForm(props.form);
    }
    else{
      let form=formLib.findInhouseForm(formId);
    if(form!=null){
      setLoadState(10);
      renderForm(form);
    }else{
      try {
        const request=await base.util.fetchRequest("/form/get-form/"+formId,'GET');
        const response=await request.json();
        if(id!=="0"){
          if(props.record===null){
            const recordRequest=await api.getRecord(base.util.getApiUrl()+response.retrieveApi,id);
            record.current=recordRequest;
          }else if(props.record!=null){
            record.current=props.record;
          }
        }
        if(response.setting){
          const recordRequest=await api.getRecordSingle(base.util.getApiUrl()+response.retrieveApi);
          record.current=recordRequest;
        }
        renderForm(response);
      } catch (error) {
        
      }
    }
    }
    
  }
  const renderForm=(formObj:any)=>{
    const renderModel=new Promise<void>((resolve,reject)=>{
      if(formObj.stateModel){
        setFormLay({
          urlLocation:location.pathname,
          formName:formObj.name,
          postapi:formObj.postApi,
          updateapi:formObj.updateApi,
          deleteapi:formObj.deleteApi,
          headers:formObj.headers,
          redirect:formObj.redirect,
          retrieveApi:base.util.getApiUrl()+formObj.retrieveApi,
          settingOverride:formObj.setting,
          form:formObj.form
        });
        stateModel.current=originalFormSet(formObj.stateModel);
        resolve();
      }else{
        reject();
      }
    });
    renderModel.then(
      function(){
        let errorJson={};
        for( let i=0; i<formObj.stateModel.length; i++){
          let key=formObj.stateModel[i].key;
          keyArr.current.push(key);
          errorJson=Object.assign({}, errorJson, { [key]: '' });
        }
        setFormError(errorJson);
        setLoadState(50);
        renderFormFields(formObj.form,keyArr.current,formObj.setting);
      },
      // display error message
      function(){
        setError(false);
        setLoad(false);
      }
    );
  }
  const originalFormSet=(stateModel:Array<Record<string,any>>)=>{
    let clear:any={};
    if(stateModel.length>0){
      for(let i=0; i<stateModel.length; i++){
        let part=stateModel[i];
        let key=part.key;
        let error:any={};
        error[key]='';
        errorClearRef.current.push(error);
        let value=part.value;
        if(value==='true'){
          value=true;
        }else if(value==='false') value=false;
        clear[part.key]=value;
      }
    }
    return clear;
  }
  const clearForm=(update:boolean=false):void=>{
    record.current={};
    const handle=formHandleRef.current;
    handle.newRecord();
    id="0";
    formUpdate.current=false;
    setFormErrorMsg({
        hide:true,
        error:""
    });
    if(!entryUsed)nav("/form/"+formId+"/0",{ state:{ login:true} });
    if(props.entry&&props.clearHandle){
      props.clearHandle(update);
    }
  }

  const clearError=()=>{
    //writeError(errorClearRef.current);
  }
  const deleteRecord=async()=>{
    if(id!="0"){
      try{
        const request=await base.util.fetchRequest(formLay.deleteapi+id,"DELETE");
        if(await request.ok){
          clearForm(true);
        }
      }catch(err){
        base.util.unauthorisedAccess();
      }
    }
  }
  const renderFormFields=(fieldArr:Array<any>,keyArr:Array<string>,settingOverride:boolean)=>{
    let fields: any[]=[];
    var fieldForArr:any[]=[];
    const renderFields=new Promise<void>((resolve,reject)=>{
      const fieldsTotal=fieldArr.length;
      let count=0;
      let loadAdd=50/fieldsTotal;
      let newCompArr=new Array(fieldArr.length);
      formCompRef.current=newCompArr;
      for(let i=0; i<fieldArr.length; i++){
        let field=fieldArr[i];
        let comp=formLib.findComponent(field.component);
        if(comp==null){
          reject();
          break;
        }
        let formErr=formError[keyArr[count]];
        let setValue=stateModel.current[keyArr[count]];
        if(id!="0"||settingOverride){
          setValue=record.current[keyArr[count]];
        }
        if(comp!=null){
        var compAtt={
          // field props for component
          field:field,
          // warning state
          error:formErr,
          // component from library
          component:comp,
          // name
          name:keyArr[count],
          //value
          value:setValue,
          // form ref handle
          formRef:formHandleRef
        };
        let fieldElement=React.createElement(comp.name,{label:field,name:keyArr[count],value:setValue,formRef:formHandleRef,ref:(ele)=>formCompRef.current[i]=ele});
        fieldForArr.push(fieldElement);
        fields.push(compAtt);
        setLoadState(loadSate+loadAdd);
        count++;
        }
      }
      if(fieldsTotal===count){
        setFormArr(fields);
        resolve();
      }else reject();
    });
    renderFields.then(
      function(){
        //loadData();
        setLoad(false);
        setSubmitBtn(false);
      },
      function(){
        setError(false);
        setLoad(false);
      }
    )
  }

  const writeError=async(errorArr:any)=>{
    for(let i=0; i<errorArr.length; i++){
      let value=errorArr[i];
      let key=Object.keys(value)[0];
      if(key!==""){
        let error:any=value[key];
        setFormError({...formError,[key]:error});
        let errorEle=document.getElementById(key+'Warning') as HTMLElement;
        errorEle.innerHTML=error;
      }
    }
  }
  const submitFormHandle=async()=>{
    const formHandle=formHandleRef.current;
    const response=formHandle.response;
    submissionDirect(response);
  }
  
  const submissionDirect=async (request:any)=>{
      if(request.ok){
        id="0";
        if(entryUsed)clearForm(true);
        if(!entryUsed){
          nav(formLay.redirect,{ state:{ login:true} });
        }else if(props.submitHandle) {
          props.submitHandle();
        }
        writeError(errorClearRef.current);
        setFormErrorMsg({
          hide:true,
          error:""
        });
      }else
      {
        const formHandle=formHandleRef.current;
        if(entryUsed)formUpdate.current=true;
        const response=await request.json();
        // show errors base on form object
        if(base.isFormGen(response)){
          const err:ErrorGenOutput=response;
          for(let x in err.fields){
            let errorJson=err.fields[x];
            if(errorJson){
              let name=Object.keys(errorJson)[0];
              let error=errorJson[name];
              formHandle.setWarning(name,error);
            }
          }
          if(err.error!==""){
            setFormErrorMsg({
              hide:false,
              error:err.error
            });
          }else if(err.error==""){
            setFormErrorMsg({
              hide:true,
              error:""
            });
          }
        }else if(base.isResponse(response)){
          const err:ResponseOutput=response;
          if(err.messageResponse!==""){
            setFormErrorMsg({
              hide:false,
              error:err.messageResponse
            });
  
          }else{
            setFormErrorMsg({
              hide:true,
              error:err.messageResponse
            });
          }
        }
      }
    }

  const changeForm=()=>{
    let result=false;
    if(formLay.urlLocation!=currentForm&&formLay.urlLocation!=""){
      result=true;
    }
    return result;
  }
  const entryHandle=()=>{
    if(props.entry){
      entryUsed=true;
      if(props.record&&props.record!=null&&!props.clearAction){
        let rec=props.record;
        if(!formUpdate.current||rec!=null){
          //clearForm();
          //record.current=rec;
          id=props.id;
        }
      }else id="0";
    }
  }

  const setEntry=()=>{
    if(props.entry) entryUsed=true;
  }
  
  useImperativeHandle(ref,()=>{
    return {
      clearForm,
      clearError
    }
  },[]);
  
  if(changeForm()){
    setFormProp();
  }
  entryHandle();
  
  useEffect(() => {
     setFormProp();
  },[]);
  return(
    <div>
    <Group>
    <Row>
    <Col >
    {!load?
    <BackButton url={-1} onClick={props.onClick}/>
    :null}
    </Col>
    <Col md={8} xs={12}>
    <div id="FormDiv">
    <div className="CentreText">
    {!entryUsed?
    <h1>{formLay.formName}</h1>
    :null}
    </div>
    <Group>
    <div id="ErrorDiv2" hidden={error}>
    <h2>Something went wrong please try again</h2> 
    </div>
    <FormHandle streamOverride={true} ref={formHandleRef} recordLayout={stateModel.current} idKey={"_id"} record={record.current} post={formLay.postapi} put={formLay.updateapi} onSubmit={submitFormHandle}>
    {load ?
      <div className='CentreText'>
      <ProgressBar now={loadSate} />
      </div>
      :null}
    {
      // writes form fields
      formArr.map((formComp,key:number)=>(
        React.createElement(formComp.component,
          {label:formComp.field.label,type:formComp.field.type,name:formComp.name,required:formComp.field.required,rows:formComp.field.rows,warning:formComp.error,value:formComp.value,formRef:formComp.formRef,ref:(ele:any)=>formCompRef.current[key]=ele})
      ))
    }
    <div id="ErrorDiv" hidden={error}>
    <h2>Something went wrong please try again</h2> 
    </div>
    <Alert variant='warning' key='warning' hidden={formErrorMsg.hide}>
    {formErrorMsg.error}
    </Alert>
    <Stack direction="horizontal" gap={1}>
    {!formLay.settingOverride?
    <Button id="ClearBtn" variant="primary" size="lg" onClick={()=>clearForm(false)} >Clear</Button>
    :null}
    {formLay.deleteapi!==""? 
    <Button id="DeleteBtn" variant={'danger'} onClick={deleteRecord} size="lg" >Delete</Button>
    :null}
    {!submitBtn?
    <SaveButton id="SubmitBtn" caption={""} size={"lg"} type={undefined}/>
    :null}
    </Stack>
    </FormHandle>
    </Group>
    </div>
    </Col>
    <Col>
    </Col>
    </Row>
    </Group>
    </div>);
});
export default form;
