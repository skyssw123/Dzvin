<?php
/**
 * Created by PhpStorm.
 * User: Vladislav Litovka
 * Date: 12/20/13
 * Time: 2:23 PM
 */

namespace Application\Model;

use Zend\Db\Adapter\Adapter;
use Zend\Db\ResultSet\ResultSet;
use Zend\Db\TableGateway\AbstractTableGateway;
use Zend\Db\Sql\Sql;

/**
 * Class UsersTable
 * @package Application\Model
 */
class UsersTable extends AbstractTableGateway  {
    /**
     * @var string
     */
    protected $table = 'users';
    /**
     * @var \Zend\Db\Adapter\Adapter
     */
    protected $adapter;

    /**
     * @param Adapter $adapter
     */
    public function __construct(Adapter $adapter) {
        $this->adapter = $adapter;

        $this->resultSetPrototype = new ResultSet();
        $this->resultSetPrototype->setArrayObjectPrototype(new User());

        $this->initialize();
    }

    /**
     * @return int
     */
    public function getTotalCount() {
        $statement = $this->adapter->createStatement('SELECT COUNT(id) as cnt FROM ' . $this->table);
        $results = $statement->execute();
        $row = $results->current();
        return is_array($row) && isset($row['cnt']) ? (int) $row['cnt'] : 0;
    }
} 