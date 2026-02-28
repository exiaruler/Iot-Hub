'use client'
import { Component } from "react";
import { Row, Col, Form, Stack } from "react-bootstrap";
import TextInput from "./TextInput";
import InputBase from "@/components/input/InputBase";
import { InputInterface, State as BaseState } from "@/components/input/interface/input";
import Warning from "@/components/formGenComponents/Warning";

interface Props extends InputInterface{
    seconds:boolean;
    mins:boolean;
    hour:boolean;
}
interface State extends BaseState{
    value:number;
    seconds:number;
    min:number;
    hour:number;
}

// time input that converts seconds,mins and hours to milliseconds
export default class TimeInput extends InputBase{
    
    public value=0;
    public seconds=0;
    public min=0;
    public hour=0;
    declare state:State;
    declare props:Props;
    constructor(props:Props) {
            super(props);
            this.state = {
                value:0,
                seconds:0,
                min:0,
                hour:0,
                warning:""
            };
    }
    componentDidMount(): void {
        debugger
        if(this.props.value){
            this.setTimeRead(this.props.value);
        }
        this.formHandleValueSet();
    }
    public setTimeRead(timeValue:number):void{
        let time=this.convertMiliSecondsToTime(timeValue);
        this.value=timeValue;
        this.seconds=time.seconds;
        this.min=time.minutes;
        this.hour=time.hours;
        let state= {
            value:this.props.value,
            seconds:this.seconds,
            min:this.min,
            hour:this.hour,
            warning:""
        };
        this.setState(state);
    }
    public formHandleValueSet():void{
        if(this.props.formRef&&this.props.name){
            debugger
            let curr=this.props.formRef.current;
            if(curr!=null&&this.warningComponent.current!=null) curr.addInput(this.warningComponent);
            if(curr!=null&&curr.props.record!=null&&Object.keys(curr.props.record).length>0){
                let val=curr.props.record[this.props.name];
                this.setTimeRead(val);
                this.value=val;
            }
            else if(curr!=null&&this.props.name)
            {
                let val=curr.props.recordLayout[this.props.name];
                this.value=val;
            }
        }
    }

    // return error value
    public formHandleValueSetUpdate(prevProps: Readonly<InputInterface>):void{
        if(prevProps.formRef&&this.props.formRef&&this.props.name){
            let prevCurr=prevProps.formRef.current||this.props.formRef.current;
            let curr=this.props.formRef.current;
            if(JSON.stringify(curr.props.record)!=='{}'&&curr.props.record!=null){
                let pastId=prevCurr.state.id;
                let currId=curr.props.record[prevCurr.props.idKey];
                if(pastId!==currId){
                    let val=curr.props.record[this.props.name];
                    this.setTimeRead(val);
                    this.value=val;
                }

            }else{
                let val=curr.props.recordLayout[this.props.name];
                this.value=val;
            }
        }
    }
    
    public convertMiliSecondsToTime(mills:number): { hours: number; minutes: number; seconds: number }{
        const totalSeconds = Math.floor(mills / 1000);
        const seconds = totalSeconds % 60;
        const totalMinutes = Math.floor(totalSeconds / 60);
        const minutes = totalMinutes % 60;
        const hours = Math.floor(totalMinutes / 60);
        return { hours, minutes, seconds };
    }
    public onChangeSeconds(seconds:number,mins:number,hours:number,event:React.ChangeEvent<HTMLInputElement>):void{
        let value=0;
        let stateCpy={...this.state};
        if(this.props.seconds){
            value+=this.secondsToMills(seconds);
            stateCpy.seconds=seconds;
            this.seconds=seconds;
        }
        if(this.props.mins){
            value+=this.minsToMills(mins);
            stateCpy.min=mins;
            this.min=mins
        }
        if(this.props.hour){
            value+=this.hoursToMills(hours);
            stateCpy.hour=hours;
            this.hour=hours;
        }

        this.value=value;
        stateCpy.value=value;
        //this.setState({...this.state,...stateCpy});
        if(this.props.formRef){
            let form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props?.onChange(event);
        }
    }

    public secondsToMills(seconds:number):number{
        const millisecondsFromSeconds = seconds * 1000;
        return millisecondsFromSeconds;
    }
    public minsToMills(mins:number):number{
        const millisecondsFromMinutes = mins * 60 * 1000;
        return millisecondsFromMinutes;
    }
    public hoursToMills(hours:number):number{
        const miliSecondsFromHours=hours* 3600000;
        return miliSecondsFromHours;
    }
    render(){
        return(
            <Row>
            <Col xs={this.props.xs} md={this.props.md}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Stack direction="horizontal" gap={2}>
            {this.props.hour?
            <TextInput label={"Hours"} type={"number"} rows={0} required={this.props.required} value={this.state.hour}
            onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChangeSeconds(this.seconds,this.min,Number.parseInt(event.target.value),event)}/>
            :null}
            {this.props.mins?
            <TextInput label={"Minutes"} type={"number"} rows={0} required={this.props.required} value={this.state.min} 
            onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChangeSeconds(this.seconds,Number.parseInt(event.target.value),this.hour,event)}/>
            :null}
            {this.props.seconds?
            <TextInput label={"Seconds"} type={"number"} rows={0} required={this.props.required} value={this.state.seconds} 
            onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChangeSeconds(Number.parseInt(event.target.value),this.min,this.hour,event)}/>
            :null}
            </Stack>
            </div>
            <div hidden={true}>
            <Form.Control readOnly={this.props.readOnly} disabled={this.props.disable} style={{width:this.props.size}} id={this.props.name+"Text"} required={this.props.required} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)}  name={this.props.name} type={this.type} value={this.getStateValue()} />
            </div>
            <Warning name={this.name} warning={this.getWarning()} ref={this.warningComponent}/>
            </Form.Group>
            </Col>
            </Row>
        );
    }
}