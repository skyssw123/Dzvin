<?php
/**
 * Created by PhpStorm.
 * User: Vladislav Litovka
 * Date: 12/21/13
 * Time: 10:45 AM
 */

namespace Application\Model;

use Zend\Db\Adapter\Adapter;
use Zend\Db\ResultSet\ResultSet;
use Zend\Db\TableGateway\AbstractTableGateway;
use Zend\Db\Sql\Sql;

/**
 * Class NotificationsTable
 * @package Application\Model
 */
class NotificationsTable extends AbstractTableGateway {
    /**
     * @var string
     */
    protected $table = 'notifications';
    /**
     * @var Adapter
     */
    protected $adapter;

    /**
     * @param Adapter $adapter
     */
    public function __construct(Adapter $adapter)
    {
        $this->adapter = $adapter;

        $this->resultSetPrototype = new ResultSet();
        $this->resultSetPrototype->setArrayObjectPrototype(new Notification());

        $this->initialize();
    }

    /**
     * @return array
     */
    public function fetchAll()
    {
        $resultSet = $this->select();

        $data = array();
        foreach ($resultSet as $item) {
            $data[] = $item;
        }
        return $data;
    }

    /**
     * @return int
     */
    public function getTotalCount()
    {
        $statement = $this->adapter->createStatement('SELECT COUNT(id) as cnt FROM ' . $this->table);
        $results = $statement->execute();
        $row = $results->current();
        return is_array($row) && isset($row['cnt']) ? (int) $row['cnt'] : 0;
    }

    /**
     * @param $id
     * @return mixed
     * @throws \Exception
     */
    public function getNotification($id)
    {
        $rowset = $this->select(array('id' => (int) $id));
        $row = $rowset->current();

        if (!$row) {
            throw new \Exception("Could not find country row $id");
        }
        return $row;
    }

    /**
     * @param $id
     * @return mixed
     */
    public function deleteNotification($id)
    {
        return $this->delete(array('id' => $id));
    }

    /**
     * @param Notification $notify
     * @return int
     * @throws \Exception
     */
    public function saveNotification(Notification $notify)
    {
        $data = array(
            'pushText'  => $notify->pushText,
            'fullText'  => $notify->fullText,
            'objectId'  => $notify->objectId,
            'startDate' => $notify->startDate,
            'endDate'   => $notify->endDate,
            'isSimple'  => $notify->isSimple,
            'rank'      => $notify->rank,
            'points'    => $notify->points,
        );

        $id = (int) $notify->id;

        if (!$id) {
            $this->insert($data);
            return $this->getLastInsertValue();
        } elseif ($this->getNotification($id)) {
            $this->update($data, array('id' => $id));
            return $id;
        } else {
            throw new \Exception('Country id does not exist');
        }
    }
} 