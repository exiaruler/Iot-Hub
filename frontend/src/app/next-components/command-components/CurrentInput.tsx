'use client'
import { Button, Col, Form, Row } from "react-bootstrap";
import { Props } from "@/components/formGenComponents/interface/textInterface";
import InputBase from "@/component-base/input/InputBase";
import {State as BaseState} from "@/component-base/input/InputBase";
import Warning from "@/components/formGenComponents/Warning";
import { RecordContext } from "@/component-base/form/FormHandle";

interface State extends BaseState{
    sliderVisible:boolean;
}

export default class CurrentInput extends InputBase{
    declare state:State;
    static contextType = RecordContext;
    constructor(props:Props) {
            super(props);
            this.state = {
                value:0,
                warning:"",
                sliderVisible:true
            };
    }
    public value:any=0;
    public type='number';

    private setCurrentValue(value:number, event:React.ChangeEvent<HTMLInputElement>):void{
        this.value=value;
        this.setState({...this.state,value:value});
        if(this.props.formRef){
            const form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange) this.props.onChange(event);
    }

   
    public onChange(event:React.ChangeEvent<HTMLInputElement>):void{
        let value=Number.parseInt(event.target.value,10);
        if(value>255) value=255;
        if(value<0) value=0;
        if(isNaN(value)) value=this.state.value;
        this.setCurrentValue(value,event);
    }

    private onSliderChange(event:React.ChangeEvent<HTMLInputElement>):void{
        this.setCurrentValue(Number.parseInt(event.target.value,10),event);
    }
   
     render(){
        const sliderVisible=this.state.sliderVisible;
        const value=Number(this.getStateValue())||0;
        return(
            <Row>
           
            <Col xs={this.props.xs} md={this.props.md}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Button type="button" variant="outline-secondary" size="sm" className="mb-2"
                aria-expanded={sliderVisible}
                onClick={()=>this.setState(prevState=>({...prevState,sliderVisible:!sliderVisible}))}>
                {sliderVisible ? "Text" : "Slider"}
            </Button>
            {sliderVisible ? <>
            <Form.Range min={0} max={255} step={1} disabled={this.props.disable||this.props.readOnly}
                aria-label={`${this.props.label || this.props.name || "Value"} slider`}
                value={value} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onSliderChange(event)} />
            <div className="small text-muted mb-2">{Math.round(value*100/255)}%</div>
            </> :
            <Form.Control readOnly={this.props.readOnly}
             disabled={this.props.disable} 
             style={{width:this.props.size}} 
             id={this.props.name+"Text"} 
             required={this.props.required} 
             onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)}  
             name={this.name} type={this.type} value={this.getStateValue()} 
             />
            }
            </div>
            <Warning name={this.name} warning={this.getWarning()} ref={this.warningComponent}/>
            </Form.Group>
            </Col>
            </Row>
            
        );
    }
}
