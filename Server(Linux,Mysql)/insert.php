<?php 
    error_reporting(E_ALL); 
    ini_set('display_errors',1);
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android"); 

    include('dbcon.php');


    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {
	
	   # 아이디
        $id=$_POST['id'];
	   # 비밀번호
        $pwd=$_POST['pwd'];
	   # 닉네임
	    $nickname=$_POST['nickname'];
        

        if(!isset($errMSG)) 
        {
            try{
                
                $stmt = $con->prepare('INSERT INTO user(id, pwd, nickname) VALUES(:id, :pwd, :nickname)');
                $stmt->bindParam(':id', $id);
                $stmt->bindParam(':pwd', $pwd);
		        $stmt->bindParam(':nickname', $nickname);

                if($stmt->execute())
                {
                    $successMSG = "php(insert)새로운 사용자를 추가했습니다.";
                }
                else
                {
                    $errMSG = "php(insert)사용자 추가 에러";
                }

            } catch(PDOException $e) {
                die("Database error: " . $e->getMessage()); 
            }
        }

    }

?>


<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

    if (!$android)
    {
?>
<html>

<body>
    <form action="<?php $_PHP_SELF ?>" method="POST">
        ID: <input type="text" name="id" /><br> 
        Password: <input type="text" name="pwd" /><br> 
        Nickname: <input type="text" name="nickname" /><br>
        <input type="submit" name="submit" />
    </form>
</body>

</html>

<?php 
    }
?>
