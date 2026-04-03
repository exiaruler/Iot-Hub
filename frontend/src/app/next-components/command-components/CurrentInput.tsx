'use client'
import { Col, Form, Row } from "react-bootstrap";
import { Props,State } from "@/components/formGenComponents/interface/textInterface";
import InputBase from "@/component-base/InputBase";
import Warning from "@/components/formGenComponents/Warning";
import { RecordContext } from "@/components/form/FormHandle";
export default class CurrentInput extends InputBase{
    static contextType = RecordContext;
    constructor(props:Props) {
            super(props);
            this.state = {
                value:0,
                warning:""
            };
    }
    public value:any=0;
    public type='number';

   
    public onChange(event:React.ChangeEvent<HTMLInputElement>):void{
        let value=Number.parseInt(event.target.value);
        if(value>255) value=255;
        if(value<0) value=0;
        if(isNaN(value)) value=this.state.value;
        this.value=value;
        this.setState({...this.state,value:value});
        
        if(this.props.formRef){
            let form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props?.onChange(event);
        }
    }
     render(){
        return(
            <Row>
           
            <Col xs={this.props.xs} md={this.props.md}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Form.Control readOnly={this.props.readOnly}
             disabled={this.props.disable} 
             style={{width:this.props.size}} 
             id={this.props.name+"Text"} 
             required={this.props.required} 
             onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)}  
             name={this.name} type={this.type} value={this.getStateValue()} 
             />
            </div>
            <Warning name={this.name} warning={this.getWarning()} ref={this.warningComponent}/>
            </Form.Group>
            </Col>
            </Row>
            
        );
    }
}