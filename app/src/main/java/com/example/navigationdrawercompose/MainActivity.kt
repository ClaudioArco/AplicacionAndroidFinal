package com.example.navigationdrawercompose
import com.example.navigationdrawercompose.retrofitjsonjetpack.UserInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigationdrawercompose.ui.theme.NavigationDrawerComposeTheme
import com.example.navigationdrawercompose.ui.theme.UserInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URL
/*
import com.example.navigationdrawercompose.retrofitjsonjetpack.UserInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.navigationdrawercompose.ui.theme.NavigationDrawerComposeTheme
import com.example.navigationdrawercompose.ui.theme.UserInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URL
*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationDrawerComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}


@Composable
fun MainScreen() {

    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState) },
        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
        }
    ) {
        Navigation(navController = navController)
    }

}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {

    TopAppBar(
        title = { Text(text = "Visualizar", fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = Color(0xFFBB86FC),
        contentColor = Color.Black
    )

}

@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController) {

    val items = listOf(
        NavegacionItem.Home,
        NavegacionItem.Profile,
        NavegacionItem.Settings,

    )

    Column(
        modifier = Modifier
            .background(color = Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.Black),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "",
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxWidth()
                    .padding(10.dp)
            )

        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { items ->
            DrawerItem(item = items, selected = currentRoute == items.route, onItemClick = {

                navController.navigate(items.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }

                scope.launch {
                    scaffoldState.drawerState.close()
                }

            })
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Claudio Arco",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        )

    }
}

@Composable
fun DrawerItem(item: NavegacionItem, selected: Boolean, onItemClick: (NavegacionItem) -> Unit) {
    val background = if (selected) R.color.purple_200 else android.R.color.transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .height(45.dp)
            .background(colorResource(id = background))
            .padding(start = 10.dp)
    ) {

        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(Color.Black),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            color = Color.Black
        )

    }

}

@Composable
fun HomeScreen() {
    
    insert()

   /* Image(
        imageVector = (R.drawable.boredape),
        contentDescription = "Imagen",
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(100.dp)),
        contentScale = ContentScale.FillWidth)*/

    /*Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {*/
/*
        Text(
            text = "Home Screen",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )*/


}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        delete()
       /*Text(
            text = "Profile Screen",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )*/


    }
}
@Composable
fun insert() {
    var textFieldValueMarca by rememberSaveable { mutableStateOf("") }
    var textFieldValueModelo by rememberSaveable { mutableStateOf("") }
    var textFieldValuePrecio by rememberSaveable { mutableStateOf("") }
    val context= LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize() ,
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        Spacer(modifier = Modifier.padding(40.dp))
        Text(
            text = "INSERTAR",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )


        TextField(
            value = textFieldValueMarca,
            onValueChange = { nuevo ->
                textFieldValueMarca = nuevo
            },
            label = {
                Text(text = "Introducir marca")
            },
            modifier = Modifier
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right)
        )


        TextField(
            value = textFieldValueModelo,
            onValueChange = { nuevo ->
                textFieldValueModelo = nuevo
            },
            label = {
                Text(text = "Introducir modelo")
            },
            modifier = Modifier
                .padding( 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right)
        )


        TextField(
            value = textFieldValuePrecio,
            onValueChange = { nuevo ->
                textFieldValuePrecio = nuevo
            },
            label = {
                Text(text = "Introducir precio")
            },
            modifier = Modifier
                .padding( 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(textAlign = TextAlign.Right)
        )

        Spacer(Modifier.height(20.dp) )


        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .size(width = 100.dp, height = 50.dp)
            ,


            onClick = {
                if(textFieldValueMarca.isEmpty()||textFieldValueModelo.isEmpty()||textFieldValuePrecio.isEmpty()){
                    Toast.makeText(context,"Rellene los campos", Toast.LENGTH_SHORT).show()

                }
               else {

                    insertar(textFieldValueMarca, textFieldValueModelo, textFieldValuePrecio)
                    Toast.makeText(context,"Datos correctos", Toast.LENGTH_LONG).show()
                    textFieldValueMarca = ""
                    textFieldValueModelo = ""
                    textFieldValuePrecio = ""
                }
            }
        ){
            Text(text = "Insert"
            )
        }


    }

}



fun insertar(marca:String,modelo:String,precio:String){

    val url = "http://iesayala.ddns.net/claudio/insertmoviles.php/?marca=$marca&modelo=$modelo&precio=$precio"

    leerUrl(url)

}
@Composable
fun delete() {

    var textFieldValueModelo by rememberSaveable { mutableStateOf("") }
    val context= LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize() ,
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        Text(
            text = "SQL DELETE",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

/*
        TextField(
            value = textFieldValueMarca,
            onValueChange = { nuevo ->
                textFieldValueMarca = nuevo
            },
            label = {
                Text(text = "Introducir marca")
            },
            modifier = Modifier
                .padding(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right)
        )

*/
        TextField(
            value = textFieldValueModelo,
            onValueChange = { nuevo ->
                textFieldValueModelo = nuevo
            },
            label = {
                Text(text = "Introducir modelo")
            },
            modifier = Modifier
                .padding( 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            textStyle = TextStyle(textAlign = TextAlign.Right)
        )

/*
        TextField(
            value = textFieldValuePrecio,
            onValueChange = { nuevo ->
                textFieldValuePrecio = nuevo
            },
            label = {
                Text(text = "Introducir modelo")
            },
            modifier = Modifier
                .padding( 10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(textAlign = TextAlign.Right)
        )
*/
        Spacer(Modifier.height(20.dp) )


        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .size(width = 100.dp, height = 50.dp)
            ,


            onClick = {
                if(textFieldValueModelo.isEmpty()){
                    Toast.makeText(context,"Rellene los campos", Toast.LENGTH_SHORT).show()

                }
                else {

                    borrar( textFieldValueModelo)
                    Toast.makeText(context,"Eliminado", Toast.LENGTH_LONG).show()

                    textFieldValueModelo = ""

                }

            }
        ){
            Text(text = "Borrar"
            )
        }


    }

}
fun borrar(modelo:String){

    val url = "http://iesayala.ddns.net/claudio/deletemoviles.php/?&modelo=$modelo"

    leerUrl(url)

}
@Composable
fun cargarJson(): UserInfo {

    var users by rememberSaveable { mutableStateOf(UserInfo()) }
    val user = UserInstance.userInterface.userInformation()

    user.enqueue(object : Callback<UserInfo> {
        override fun onResponse(
            call: Call<UserInfo>,
            response: Response<UserInfo>
        ) {
            val userInfo: UserInfo? = response.body()
            if (userInfo != null) {
                users = userInfo
            }
        }

        override fun onFailure(call: Call<UserInfo>, t: Throwable)
        {
            Log.d("datos",users.toString())
        }

    })

    return users
}





fun leerUrl(urlString:String){
    GlobalScope.launch(Dispatchers.IO)   {
        val response = try {
            URL(urlString)
                .openStream()
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            "Error with ${e.message}."
            Log.d("io", e.message.toString())
        } catch (e: Exception) {
            "Error with ${e.message}."
            Log.d("io", e.message.toString())
        }
    }

    return
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



                /* Image(
                     painter = rememberImagePainter(0.avatar_url),
                     contentDescription = "Imagen",
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(10.dp)
                         .height(150.dp)
                         .clip(RoundedCornerShape(100.dp)),
                     contentScale = ContentScale.FillWidth
                 )*/

            Llamada()


        }

    }

@Composable
fun Llamada() {
    var lista= cargarJson()
    Row(){
        Column(modifier = Modifier.weight(4f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text="Marca",
                color = Color(0xFFBB86FC),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(20.dp),

                )
        }
        Column(modifier = Modifier.weight(4f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text="Modelo",
                color = Color(0xFFBB86FC),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(20.dp),

                )
        }
        Column(modifier = Modifier.weight(4f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text="precio",
                color = Color(0xFFBB86FC),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(20.dp),

                )
        }
    }
    LazyColumn()

    {
        items(lista) { usu ->
            Box(
                Modifier
                    .background(Color.White)
                    .width(370.dp)){
                Row(){
                    Column(modifier = Modifier.weight(4f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text=usu.marca,
                            color = Color.Blue,
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp,
                            modifier = Modifier
                                .padding(25.dp),

                            )
                    }
                    Column(modifier = Modifier.weight(3f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text=usu.modelo,
                            color = Color.Blue,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(25.dp),
                        )
                    }
                    Column(modifier = Modifier.weight(3f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        Text(text=usu.precio.toString(),
                            color = Color.Blue,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(25.dp),
                        )
                    }

                }


            }
            Spacer(
                modifier = Modifier
                    .height(5.dp)
            )


        }
    }


}

    @Composable
    fun ShareScreen() {

        /*Text(
            text = "Share Screen",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )*/

    }


    @Composable
    fun ContactScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Contacto",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )

        }
    }

    @Composable
    fun Navigation(navController: NavHostController) {

        NavHost(navController, startDestination = NavegacionItem.Home.route) {

            composable(NavegacionItem.Home.route) {
                HomeScreen()
            }

            composable(NavegacionItem.Profile.route) {
                ProfileScreen()
            }

            composable(NavegacionItem.Settings.route) {
                SettingsScreen()
            }


        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        NavigationDrawerComposeTheme {
            MainScreen()
        }

    }
