

<?php

$server = "localhost";
$user = "root";
$pass = "clave";
$bd = "CL";

//Creamos la conexión
$conexion = mysqli_connect($server, $user, $pass,$bd)
or die("Ha sucedido un error inexperado en la conexion de la base de datos");

//generamos la consulta
$sql = "SELECT * FROM movil";
mysqli_set_charset($conexion, "utf8"); //formato de datos utf8

if(!$result = mysqli_query($conexion, $sql)) die();

$moviles = array(); //creamos un array

while($row = mysqli_fetch_array($result))
{
    $marca=$row['marca'];
    $modelo=$row['modelo'];
    $precio=$row['precio'];


    $moviles[] = array('marca'=> $marca, 'modelo'=> $modelo, 'precio'=> $precio);

}

//desconectamos la base de datos
$close = mysqli_close($conexion)
or die("Ha sucedido un error inexperado en la desconexion de la base de datos");


//Creamos el JSON
$json_string = json_encode($moviles);
echo $json_string;
?>



