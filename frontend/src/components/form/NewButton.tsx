import { ButtonComponent } from "../../component-base/ButtonComponent"
import { ButtonProps as base} from "../../component-base/ButtonComponent";
import FormHandle from "../../component-base/form/FormHandle";
interface ButtonProps extends base{
    formRef:FormHandle|any;
}
export default class NewButton extends ButtonComponent{
    declare props:ButtonProps;
    caption=this.props.caption||"Clear";

    public onClick(event:React.MouseEvent<HTMLButtonElement>):void{
        if(this.props.formRef){
            let form=this.props.formRef.current;
            form.newRecord();
        }
        if(this.props.onClick){
            this.props.onClick(event);
        }
   }
}