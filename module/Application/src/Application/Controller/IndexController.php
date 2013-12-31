<?php
/**
 * Zend Framework (http://framework.zend.com/)
 *
 * @link      http://github.com/zendframework/ZendSkeletonApplication for the canonical source repository
 * @copyright Copyright (c) 2005-2013 Zend Technologies USA Inc. (http://www.zend.com)
 * @license   http://framework.zend.com/license/new-bsd New BSD License
 */

namespace Application\Controller;

use Zend\Mvc\Controller\AbstractActionController;
use Zend\View\Model\ViewModel;
use Zend\Debug\Debug;
use Application\Model\Notification;

/**
 * Class IndexController
 * @package Application\Controller
 */
class IndexController extends AbstractActionController
{
    /**
     * @return \Zend\Http\Response|\Zend\View\Model\ViewModel
     */
    public function indexAction()
    {
        // getting instance of service locator
        $sl = $this->getServiceLocator();
        // checking for auth
        if (!$sl->get('AuthService')->hasIdentity()) {
            return $this->redirect()->toRoute('login');
        }

        // getting required models
        $notifyTable = $sl->get('Application\Model\NotificationsTable');
        return new ViewModel(array(
            'list'  => $notifyTable->fetchAll(), // getting list of all notifications
        ));
    }

    /**
     * @return \Zend\Http\Response|\Zend\View\Model\ViewModel
     */
    public function formAction()
    {
        // getting instance of service locator
        $sl = $this->getServiceLocator();
        // checking for auth
        if (!$sl->get('AuthService')->hasIdentity()) {
            return $this->redirect()->toRoute('login');
        }
        return new ViewModel();
    }

    /**
     * @return \Zend\Http\Response
     */
    public function pushAction()
    {
        // getting instance of service locator
        $sl = $this->getServiceLocator();
        // checking for auth
        if (!$sl->get('AuthService')->hasIdentity()) {
            return $this->redirect()->toRoute('login');
        }
        // getting request instance
        $request = $this->getRequest();

        // including class file
        require_once('vendor/parse-library/parse.php');

        // creating parseObject object
        $parse = new \parseObject('Alert');
        // preparing array with data

        $startDate  = date('Y-m-d H:i:s', strtotime($request->getPost('startDate')));
        $endDate    = date('Y-m-d H:i:s', strtotime($request->getPost('endDate')));
        $data = array(
            'pushText'      => $request->getPost('pushText'),
            'fullText'      => $request->getPost('fullText'),
            'isSimple'      => ((int) $request->getPost('isSimple')) ? true : false,
            'startDate'     => $parse->dataType('date', $startDate),
            'endDate'       => $parse->dataType('date', $endDate),
            'rank'          => (int) $request->getPost('rank'),
            'points'        => $request->getPost('points'),
        );
        // adding params to the parseObject
        foreach ($data as $key => $val) {
            $parse->__set($key, $val);
        }
        // saving object to parse service
        $response = $parse->save();
        // getting objectId
        $data['objectId']   = $response->objectId;
        $data['startDate']  = $startDate;
        $data['endDate']    = $endDate;
        // getting required models
        $notifyTable = $sl->get('Application\Model\NotificationsTable');
        $notify = new Notification();
        // preparing notification's data
        $notify->exchangeArray($data);

        // saving notification to DB
        $data['id'] = $notifyTable->saveNotification($notify);
        // redirecting to message page
        return $this->redirect()->toRoute('show', array(
            'controller' => 'Application\Controller\Index',
            'action' => 'show',
            'id' => $data['id']
        ));
    }

    /**
     * @return ViewModel
     */
    public function showAction()
    {
        // getting instance of service locator
        $sl = $this->getServiceLocator();

        // checking for auth
        if (!$sl->get('AuthService')->hasIdentity()) {
            return $this->redirect()->toRoute('login');
        }

        // getting message ID
        $ID = $this->params('id');
        if (!$ID) {
            throw new \Exception('Can\'t find message id');
        }

        // getting required models
        $notifyTable = $sl->get('Application\Model\NotificationsTable');

        // getting notification by message ID
        $notification = $notifyTable->getNotification($ID);

        // checking data consistency
        if (!$notification) {
            throw new \Exception('Can\'t load message info');
        }

        return new ViewModel(array(
            'notification' => $notification
        ));
    }
}