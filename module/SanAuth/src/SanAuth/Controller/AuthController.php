<?php
namespace SanAuth\Controller;

use Zend\Mvc\Controller\AbstractActionController;
use Zend\Form\Annotation\AnnotationBuilder;
use Zend\View\Model\ViewModel;

use SanAuth\Model\User;

/**
 * Class AuthController
 * @package SanAuth\Controller
 */
class AuthController extends AbstractActionController
{
    /**
     * @var
     */
    protected $form;
    /**
     * @var
     */
    protected $storage;
    /**
     * @var
     */
    protected $authservice;

    /**
     * @return object
     */
    public function getAuthService()
    {
        if (!$this->authservice) {
            $this->authservice = $this->getServiceLocator()->get('AuthService');
        }
        
        return $this->authservice;
    }

    /**
     * @return object
     */
    public function getSessionStorage()
    {
        if (!$this->storage) {
            $this->storage = $this->getServiceLocator()->get('SanAuth\Model\MyAuthStorage');
        }
        
        return $this->storage;
    }

    /**
     * @return \Zend\Form\Form
     */
    public function getForm()
    {
        if (!$this->form) {
            $user       = new User();
            $builder    = new AnnotationBuilder();
            $this->form = $builder->createForm($user);
        }
        
        return $this->form;
    }

    /**
     * @return array|\Zend\Http\Response
     */
    public function loginAction()
    {
        //if already login, redirect to success page 
        if ($this->getAuthService()->hasIdentity()) {
            return $this->redirect()->toRoute('success');
        }
                
        $form       = $this->getForm();
        
        return array(
            'form'      => $form,
            'messages'  => $this->flashmessenger()->getMessages()
        );
    }

    /**
     * @return \Zend\Http\Response
     */
    public function authenticateAction()
    {
        $form       = $this->getForm();
        $redirect   = 'login';
        
        $request = $this->getRequest();
        if ($request->isPost()) {
            $form->setData($request->getPost());
            if ($form->isValid()) {
                //check authentication...
                $this->getAuthService()->getAdapter()
                                       ->setIdentity($request->getPost('login'))
                                       ->setCredential($request->getPost('password'));
                                       
                $result = $this->getAuthService()->authenticate();
                foreach ($result->getMessages() as $message) {
                    //save message temporary into flashmessenger
                    $this->flashmessenger()->addMessage($message);
                }
                
                if ($result->isValid()) {
                    $redirect = 'home';
                    //check if it has rememberMe :
                    if ($request->getPost('rememberme') == 1 ) {
                        $this->getSessionStorage()->setRememberMe(1);
                        //set storage again
                        $this->getAuthService()->setStorage($this->getSessionStorage());
                    }
                    $this->getAuthService()->setStorage($this->getSessionStorage());
                    $this->getAuthService()->getStorage()->write($request->getPost('login'));
                }
            }
        }
        
        return $this->redirect()->toRoute($redirect);
    }

    /**
     * @return \Zend\Http\Response
     */
    public function logoutAction()
    {
        if ($this->getAuthService()->hasIdentity()) {
            $this->getSessionStorage()->forgetMe();
            $this->getAuthService()->clearIdentity();
            $this->flashmessenger()->addMessage("You've been logged out");
        }
        
        return $this->redirect()->toRoute('login');
    }
}