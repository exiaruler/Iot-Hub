'use client'

import { Form } from "react-bootstrap"
import WarningBase from "../../component-base/input/warning/WarningBase"
// display warnings
export default class WarningDisplay extends WarningBase{

    render(){
        return(
            <Form.Text id={this.props.name+"Warning"} >{this.props.warning}</Form.Text>
        )
    }
}