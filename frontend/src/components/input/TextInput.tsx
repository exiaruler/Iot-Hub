import { Row, Form } from "react-bootstrap";
import InputBase from "./InputBase";
import { Props, State } from "../formGenComponents/interface/textInterface";

export default class TextInput extends InputBase<Props,State>{
    constructor(props:Props) {
        super(props);
        this.state = {
            value:""
        };
    }
    onChange(event:React.ChangeEvent<HTMLInputElement>){
        var value=event.target.value;
        this.setState({...this.state,value:value});
                
        if(this.props.formRef){
            var form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props.onChange(event);
        }
    }
    render(){
        return(
            <Row>
            {this.column(<Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Form.Control readOnly={this.props.readOnly} disabled={this.props.disable} style={{width:this.props.size}} id={this.props.name+"Text"} required={this.props.required} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)}  name={this.props.name} type={this.props.type} defaultValue={this.props.value} />
            </div>
            <Form.Text id={this.props.name+"Warning"} >{this.props.warning} </Form.Text>
            </Form.Group>,this.props.xs,this.props.md)}
            </Row>
        )
    }
}