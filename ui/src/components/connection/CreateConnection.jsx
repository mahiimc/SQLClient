import React, { useEffect, useState } from 'react'
import { Button, Card, Form, Input, Select, Tabs, message } from 'antd'
import './Connection.css'
import axios from 'axios'
import { loadDriverClasses } from './ConnectionService';



const { TabPane } = Tabs;


const CreateConnection = () => {

    const [form] = Form.useForm();
    const [testDisabled, setTestDisabled] = useState(false);
    const [classes,setClasses] = useState(undefined);


    const handleTabChange = (key) => {
        if (key === '2') {
            console.log("Properties")
        }
    }
        

    const onFinish = () => {
        form.validateFields()
            .then(values => {
                axios.post("connection/save", values)
                    .then(async response => {
                        const data = await response.data;
                        message.success(data.message);
                        form.resetFields();

                    })
                    .catch(async error => {
                        const data = await error.response.data;
                        message.error(data.message);
                    })
            })
            .catch(error => {

            })
            
    }

    const getDriverClasses = async () => {
        loadDriverClasses().then( data => {

            setClasses(data.data.map((item,index) => (<Select.Option value={item}/>)));   
        })
        .catch(error => {
            throw error;
        })
    }

    const onTest = () => {
        setTestDisabled(true);
        form.validateFields()
            .then(values => {
                axios.post("connection/test", values)
                    .then(async response => {
                        const data = await response.data;
                        message.success(data.message);
                        setTestDisabled(false);

                    })
                    .catch(async error => {
                        const data = await error.response.data;
                        message.error(data.message);
                        setTestDisabled(false);
                    })
            })
            .catch(error => {
                console.log(error);
            })

    }

    useEffect(() => {
        getDriverClasses();
    },[])


    return (
        <div className='add-connection'>
            <Card
                title='New Connection'
                style={{
                    width: '70%'
                }}

            >
                <Tabs defaultActiveKey='1' onChange={handleTabChange} tabPosition='top'>

                    <TabPane tab="General" key="1">
                        <Form form={form} preserve={false} layout='vertical'>
                            <Form.Item
                                name="name"
                                id='name'
                                rules={
                                    [
                                        {

                                            required: true,
                                            message: "Connection name is mandotary"
                                        }
                                    ]
                                }
                            >
                                <Input placeholder='Connection name' />
                            </Form.Item>

                            <Form.Item
                                name="url"
                                id='url'
                                rules={
                                    [
                                        {
                                            required: true,
                                            message: "JDBC url required"
                                        }
                                    ]
                                }
                            >
                                <Input placeholder='Jdbc url' />
                            </Form.Item>

                            <Form.Item
                                name="driver"
                                id='driver'
                                rules={
                                    [
                                        {
                                            required: true,
                                             message: "Driver required"
                                        }
                                    ]
                                }
                            >
                                 <Select placeholder='Select Driver Class'>
                                    {classes}
                                </Select>
                            </Form.Item>
                            <div className='credentials'>
                                <Form.Item
                                    style={{ width: '50%' }}
                                    name="username"
                                    id='username'
                                >
                                    <Input placeholder='Username' />
                                </Form.Item>

                                <Form.Item
                                    style={{ width: '50%' }}
                                    name="password"
                                    id='password'
                                >
                                    <Input.Password placeholder='Password' />
                                </Form.Item>
                            </div>

                        </Form>
                    </TabPane>
                    <TabPane tab="Properties" key="2" disabled>
                        <p>Properties here </p>
                    </TabPane>
                    <TabPane tab="Advanced" key="3" disabled>
                        <p> Advanced Settings </p>
                    </TabPane>

                </Tabs>
                <div className='actions'>
                    <Button type='default' key='reset' onClick={() => form.resetFields()} >Reset</Button>
                    <Button type='default' key='test' onClick={onTest}>Test</Button>,
                    <Button type='primary' key='save' onClick={onFinish}>Save</Button>,
                </div>

            </Card>
        </div>

    )
}

export default CreateConnection