import { Col, Form, Row } from "react-bootstrap";
import { HexColorPicker } from "react-colorful";
import { Props } from './interface/textInterface';
import InputBase from "../input/InputBase";

// rgb hex select
export default class RgbHexSelect extends InputBase{
    constructor(props:Props) {
        super(props);
        this.state = {
            value:"#ffffff",
            warning:""
        };
    }
        
    render(){
        return(
            <Row>
            <Col xs={this.props.xs} md={this.props.md}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <HexColorPicker id={'Hex-Select-'+this.props.name} color={this.getStateValue()} onChange={(value)=>this.onChangeValue(value)}/>
            </div>
            <Form.Text id={this.props.name+"Warning"} >{this.getWarning()} </Form.Text>
            </Form.Group>
            </Col>
            </Row>
        )
    }
}