'use client'
import { ChangeEventHandler, Component } from "react"
import { Col, Form, Row } from "react-bootstrap";
import FormHandle from "../form/FormHandle";

type Props={
    label:string,
    name?:string,
    valueKey:string,
    displayKey:string,
    options?:any,
    api?:string,
    required?:boolean,
    readOnly?:boolean,
    disable?:boolean,
    onChange?:ChangeEventHandler,
    warning?:string,
    value?:any,
    size?:any,
    md?:number,
    xs?:number,
    formRef?:FormHandle|any;
}
type state={
    options:any;
    value:Object|null;
    displayValue:string;
    objectValue:null|Object;
    
}
export default class FormGenSelect extends Component<Props,state>{
    constructor(props:Props) {
        super(props);
        this.state = {
            options:[],
            value:null,
            displayValue:"",
            objectValue:null
        };
    }
    componentDidMount(): void {
        if(this.props.options){
            this.setState({...this.state,options:this.props.options});
        }else if(this.props.api){

        }
    }
    componentDidUpdate(prevProps: Readonly<Props>, prevState: Readonly<state>, snapshot?: any): void {
        if(this.props.options!=prevState.options){
            this.setState({...this.state,options:this.props.options});
        }
    }
    public clearOptions(){
        this.setState({...this.state,options:[]});
    }
    public getObjectValue(){
        return this.state.objectValue;
    }
    public getObjectValueKey(value:number|string){
        var val=null;
        val=this.props.options.find((rec:any)=>rec[this.props.valueKey]==value);
        return val;
    }
    public onChange(event:any){
        var value=event.target.value;
        if(this.props.valueKey){
            var objectValue=this.props.options.find((ob:any)=>ob[this.props.valueKey]==value)||"";
            this.setState({...this.state,value:value,displayValue:objectValue[this.props.displayKey],objectValue:objectValue});
        }else
        {
            this.setState({...this.state,value:value,displayValue:value[this.props.displayKey]});
        }
        if(this.props.formRef){
            var form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props.onChange(event);
        }
    }
    public setValue(index:number){
        var value=null;
        if(this.props.valueKey){
            value=this.state.options[index][this.props.valueKey];
        }else {
            value=JSON.stringify(this.state.options[index]);
        }
        return value;
    }
    render(){
        return(
            <Row>
            <Col md={this.props.md} xs={this.props.xs}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Form.Select disabled={this.props.disable} style={{width:this.props.size}} id={this.props.name+"Select"} required={this.props.required} onChange={(event:any)=>this.onChange(event)}  name={this.props.name} value={this.props.value}>
            {
                <option hidden={false}>{''}</option>
            }
            {
                this.state.options.map((opt:any,num:number)=>(
                    <option key={num} value={this.setValue(num)}>{opt[this.props.displayKey]}</option>
                ))
            }
            </Form.Select>
            </div>
            <Form.Text id={this.props.name+"Warning"} >{this.props.warning} </Form.Text>
            </Form.Group>
            </Col>
            </Row>
        );
    }
}