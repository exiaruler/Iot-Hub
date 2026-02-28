'use client'
import { Col, Row } from "react-bootstrap";
import PageGroup from "./next-components/pageGroup";
import ContentEditor from "@/components/content/ContentEditor";

export default function Home() {
  
  return (
   <div>
  <PageGroup>
  <Row>
  <ContentEditor/>
  </Row>
  </PageGroup>
   </div>
  );
}
