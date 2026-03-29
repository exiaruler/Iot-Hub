import { Button, ButtonGroup } from "react-bootstrap";
import { ButtonComponent } from "../../component-base/ButtonComponent";

export class SaveButton extends ButtonComponent{
    
    
    render(){
        return(
        <ButtonGroup className="Button-Regular">
        <Button variant={this.props.variant||"primary"} type={"submit"} size={this.props.size}>
        {this.props.caption||"Save"}
        </Button>   
        </ButtonGroup> 
        );
    }
}