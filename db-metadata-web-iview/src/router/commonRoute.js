
const commonRoute = [
    {
        path: '/',
        name: 'Main',
        redirect: '/main'
    }, {
        path: '/main',
        name: 'main',
        component: () => import('@/components/Main'),
        redirect: '/main/meta-data',
        children: [
            {
                path: 'meta-data',
                name: 'Metadata',
                component: () => import('@/components/template/MasterSlaveTableTmpl')
            }, {
                path: 'meta-manager',
                name: 'MetaManager',
                component: () => import('@/components/meta/MetaManager')
            }, {
                path: 'meta-object',
                name: 'MetaObject',
                component: () => import('@/components/meta/MetaObject')
            }, {
                path: 'meta-conf',
                name: 'MetaConf',
                component: () => import('@/components/meta/MetaConf')
            }, {
                path: 'demo',
                name: 'Demo',
                component: () => import('@/components/demo/DemoMain')
            }
        ]
    },
];
export default commonRoute
