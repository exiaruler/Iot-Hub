import { Form } from "react-bootstrap";
import WarningBase from "../input/WarningBase";

export default class Warning extends WarningBase{
    render(){
        return(
            <Form.Text id={this.props.name+"Warning"} >{this.warning}</Form.Text>
        )
    }
}