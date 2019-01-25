<?php 
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    include('user_dbcon.php');

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android ){

        # 아이디
        $id=$_POST['id'];  

        $stmt = $con->prepare("delete from orderDB where orderHis_id = '$id'");
        $stmt->execute();
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
