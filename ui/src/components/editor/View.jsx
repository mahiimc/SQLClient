import { Table } from 'antd';
import React, { useEffect, useState } from 'react'

const View = ({ cols, data, loading  }) => {

    const [columns, setColumns] = useState([]);
    const [dataSource, setDataSource] = useState([]);
   

    const prepareColumns = (cols) =>
        cols.map((column, index) => ({
            title: (<div><span>{column.name}</span></div>),
            dataIndex: column.name,
            key: column.name
        }));


    const prepareDataSource = (items) =>
        items.map((item, index) => (item));

    async function onLoad() {
        setColumns(prepareColumns(cols));
        setDataSource(prepareDataSource(data));
    }

    useEffect(() => {
        onLoad();
    }, [loading])
    return (
        <Table scroll={{ x: 'max-content', y: 500 }}
            style={{ width: '100%', height: '40vh' }}
            columns={columns}
            loading={loading}
            dataSource={dataSource} />
    )
}

export default View