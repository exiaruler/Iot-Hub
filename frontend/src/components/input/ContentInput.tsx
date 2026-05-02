'use client'
import { Editor } from "@tinymce/tinymce-react";
import { Component } from "react";
import {Util} from "../../base/Util";
import { Row, Col } from "react-bootstrap";
import FormHandle, { RecordContext } from "../form/FormHandle";
import InputBase from "../../component-base/InputBase";

export default class ContentInput extends InputBase{
    public util=new Util();
    static contextType = RecordContext;
    public apiKey=this.util.tinyKey;
    
 
    render(){
        return(
            <Row>
            <Col>
            <Editor
            onEditorChange={(value:string)=>this.onChangeValue(value)}
            apiKey={this.apiKey}
            value={this.getStateValue()}
            init={{
                height:"800px",
                plugins: [
                // Core editing features
                'anchor', 'autolink', 'charmap', 'codesample', 'emoticons', 'link', 'lists', 'media', 'searchreplace', 'table', 'visualblocks', 'wordcount'
                ],
                toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link media table mergetags | addcomment showcomments | spellcheckdialog a11ycheck typography uploadcare | align lineheight | checklist numlist bullist indent outdent | emoticons charmap | removeformat',
                tinycomments_mode: 'embedded',
                tinycomments_author: 'Author name',
                mergetags_list: [
                { value: 'First.Name', title: 'First Name' },
                { value: 'Email', title: 'Email' },
                ],
                ai_request: (request:any, respondWith:any) => respondWith.string(() => Promise.reject('See docs to implement AI Assistant')),
                uploadcare_public_key: 'dfff09e4cb6c697f606a',
            }}
            
            />
            </Col>
            </Row>
        )
    }
}