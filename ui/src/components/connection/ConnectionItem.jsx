import { InfoCircleOutlined, RightOutlined } from '@ant-design/icons'
import React from 'react'


const ConnectionItem = ({item}) => (
      {
      title: item.name,
      key: item.id,
      children: [
        {
          title: 'Tables',
          key: `${item.id}-tables`,
          isLeaf: false
        },
        {
          title: 'Views',
          key: `${item.id}-views`,
          isLeaf: false,
        },
      ]
    }
    )

export default ConnectionItem