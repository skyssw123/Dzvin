<?php
/**
 * Created by PhpStorm.
 * User: Vladislav Litovka
 * Date: 12/20/13
 * Time: 2:22 PM
 */

namespace Application\Model;

use Zend\Form\Annotation;

/**
 * Class User
 * @package Application\Model
 */
class User {
    /**
     * @var
     */
    public $id;
    /**
     * @var
     */
    public $login;
    /**
     * @var
     */
    public $password;

    /**
     * @param $data
     */
    public function exchangeArray($data)
    {
        $this->id       = isset($data['id']) ? $data['id'] : null;
        $this->login    = isset($data['login']) ? $data['login'] : null;
        $this->password = isset($data['password']) ? $data['password'] : null;
    }
} 