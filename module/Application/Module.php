<?php
/**
 * Zend Framework (http://framework.zend.com/)
 *
 * @link      http://github.com/zendframework/ZendSkeletonApplication for the canonical source repository
 * @copyright Copyright (c) 2005-2013 Zend Technologies USA Inc. (http://www.zend.com)
 * @license   http://framework.zend.com/license/new-bsd New BSD License
 */

namespace Application;

use Zend\Mvc\ModuleRouteListener;
use Zend\Mvc\MvcEvent;
use Application\Model\UsersTable;
use Application\Model\NotificationsTable;
use Zend\ModuleManager\Feature\AutoloaderProviderInterface;
use Zend\Authentication\AuthenticationService;
use Zend\Authentication\Adapter\DbTable as DbTableAuthAdapter;

class Module
{
    private $adapter;

    public function onBootstrap(MvcEvent $e)
    {
        $eventManager        = $e->getApplication()->getEventManager();
        $moduleRouteListener = new ModuleRouteListener();
        $moduleRouteListener->attach($eventManager);

        $serviceManager = $e->getApplication()->getServiceManager();
        $viewModel = $e->getApplication()->getMvcEvent()->getViewModel();
        $viewModel->sm = $serviceManager;
    }

    public function getAdapter(&$sm)
    {
        if (!$this->adapter) {
            $this->adapter = $sm->get('Zend\Db\Adapter\Adapter');
        }
        return $this->adapter;
    }

    public function getConfig()
    {
        return include __DIR__ . '/config/module.config.php';
    }

    public function getAutoloaderConfig()
    {
        return array(
            'Zend\Loader\StandardAutoloader' => array(
                'namespaces' => array(
                    __NAMESPACE__ => __DIR__ . '/src/' . __NAMESPACE__,
                ),
            ),
        );
    }

    public function getServiceConfig()
    {
        return array(
            'factories' => [
                'Application\Model\UsersTable' =>  function($sm) {
                    $table     = new UsersTable($this->getAdapter($sm));
                    return $table;
                },
                'Application\Model\NotificationsTable' =>  function($sm) {
                        $table     = new NotificationsTable($this->getAdapter($sm));
                        return $table;
                    },
                'AuthService' => function($sm) {
                    //My assumption, you've alredy set dbAdapter
                    //and has users table with columns : user_name and pass_word
                    //that password hashed with md5
                    $dbAdapter           = $this->getAdapter($sm);
                    $dbTableAuthAdapter  = new DbTableAuthAdapter($dbAdapter, 'users', 'login', 'password', 'MD5(?)');

                    $authService = new AuthenticationService();
                    $authService->setAdapter($dbTableAuthAdapter);
                    $authService->setStorage($sm->get('Application\Model\MyAuthStorage'));

                    return $authService;
                },
                'Application\Model\MyAuthStorage' => function($sm) {
                    return new \Application\Model\MyAuthStorage('zf_tutorial');
                },
            ]
        );
    }
}
