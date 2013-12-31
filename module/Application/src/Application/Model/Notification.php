<?php
/**
 * Created by PhpStorm.
 * User: Vladislav Litovka
 * Date: 12/21/13
 * Time: 10:46 AM
 */

namespace Application\Model;


/**
 * Class Notification
 * @package Application\Model
 */
class Notification {
    /**
     * @var
     */
    public $id;

    /**
     * @var
     */
    public $pushText;

    /**
     * @var
     */
    public $fullText;

    /**
     * @var
     */
    public $objectId;

    /**
     * @var
     */
    public $startDate;

    /**
     * @var
     */
    public $endDate;

    /**
     * @var
     */
    public $isSimple;

    /**
     * @var
     */
    public $rank;

    /**
     * @var
     */
    public $points;

    /**
     * @param $data
     */
    public function exchangeArray($data)
    {
        $this->id           = isset($data['id']) ? $data['id'] : null;
        $this->pushText     = isset($data['pushText']) ? $data['pushText'] : null;
        $this->fullText     = isset($data['fullText']) ? $data['fullText'] : null;
        $this->objectId     = isset($data['objectId']) ? $data['objectId'] : null;
        $this->startDate    = isset($data['startDate']) ? $data['startDate'] : null;
        $this->endDate      = isset($data['endDate']) ? $data['endDate'] : null;
        $this->isSimple     = isset($data['isSimple']) ? $data['isSimple'] : null;
        $this->rank         = isset($data['rank']) ? $data['rank'] : null;
        $this->points       = isset($data['points']) ? $data['points'] : null;
    }
} 