<?php 
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    include('user_dbcon.php');

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ){

        # 아이디
        $id=$_POST['id'];  

        $stmt = $con->prepare("select * from orderDB where orderHis_id = '$id'");
        $stmt->execute();

        if ($stmt->rowCount() > 0)
        {
            $data = array(); 

            while($row=$stmt->fetch(PDO::FETCH_ASSOC))
            {
                extract($row);
                //json 형식
                array_push($data, array('orderHis_orderName'=>$orderHis_orderName,'orderHis_price'=>$orderHis_price,'orderHis_store'=>$orderHis_store,'orderHis_date'=>$orderHis_date));
            }

            header('Content-Type: application/json; charset=utf8');
            $json = json_encode(array("orderHistory"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }

?>

<?php 

    if (!$android)
    {
?>
<html>

<body>
    <form method="POST">
        ID: <input type="text" name="id" /><br>
        <input type="submit" name="submit" />
    </form>
</body>

</html>

<?php 
    }
?>
