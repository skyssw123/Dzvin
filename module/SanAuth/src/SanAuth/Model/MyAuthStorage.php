<?php

namespace SanAuth\Model;

use Zend\Authentication\Storage;

/**
 * Class MyAuthStorage
 * @package SanAuth\Model
 */
class MyAuthStorage extends Storage\Session
{
    /**
     * @param int $rememberMe
     * @param int $time
     */
    public function setRememberMe($rememberMe = 0, $time = 1209600)
    {
        if ($rememberMe == 1) {
            $this->session->getManager()->rememberMe($time);
        }
    }

    /**
     *
     */
    public function forgetMe()
    {
        $this->session->getManager()->forgetMe();
    } 
}