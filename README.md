
##English version coming soon...
___

#The Inner Eye - Manual del Juego

Este es un manual del juego, destinado a jugadores noveles en el mundo de los *roguelike*. Es el manual para la versión 1.0, es decir, la inmediatamente siguiente a la presentación del proyecto. De ahí el primer punto, explicando que no es un juego terminado.


##Disclaimer
El juego no está terminado. Si bien tiene todas las funcionalidades implementadas existe un amplio margen para extender los contenidos. Es conveniente añadir más enemigos, más armas, etc. Por lo que seguiré con el desarrollo del juego tras presentar el proyecto, inicialmente añadiendo variedad pero también tengo algunas ideas  acerca de las cosas que quiero añadir a mayores. Por supuesto, se aceptan sugerencias en cuanto a contenido y se agradece mucho el *feedback* en cuanto a cualquier cosa que pueda mejorar.

Aparte del contenido, el juego no está aún equilibrado en lo que respecta al nivel de dificultad actual. El héroe tiene demasiada vida y daño, los enemigos apenas quitan vida, etc. Esto será arreglado, por supuesto.

La traducción a español está aún en desarrollo y existen aún puntos por pulir.

##Requisitos del sistema
El juego requiere tener instalado en el PC un entorno de ejecución Java 7.0 o superior. En caso de querer utilizar lectores de pantalla para poder jugar usando la lectura de los mensajes, Java Access Bridge debe estar correctamente instalado y configurado.

La función de lectura se ha probado y funciona correctamente con Windows 7, 8 y 8.1, todos en 32 y 64 bits, así como con los lectores [NVDA 2014.2](http://www.nvaccess.org/) y  [JAWS 15.0.11024](http://www.freedomscientific.com/Downloads/JAWS)  ambos en 32 y 64 bits.
  
##El juego
Se trata de un *roguelike* clásico. El objetivo del juego es descender por los niveles de una mazmorra, encontrar el ídolo y salir con vida. El héroe tiene una *clase*, con sus propias características y estilos de juego que afectan a la dificultad y a la puntuación obtenida con cada acción. Esta clase modifica atributos del héroe y fuerza al jugador a adoptar una serie de estrategias en función de su clase. Estrategias que con otra clase, serían más difíciles de seguir con éxito. Por ejemplo, el *guerrero* es una clase centrada en el ataque cuerpo a cuerpo y tiene poca destreza para usar el arco, lo que hace más difícil hacer daño a los enemigos con este tipo de armas, ya que requieren una alta destreza. Tampoco sabe leer, por lo que no puede usar pergaminos. A cambio, el guerrero tiene más puntos de vida que las otras clases.

Se pueden equipar *armas* y *armadura*, aumentando así nuestro ataque y nuestra defensa. Dichas armas variarán en calidad y tienen diferentes modificadores, indicando el daño extra que hace cada arma. Hay tres tipos de armas: cuerpo a cuerpo, a distancia y arrojadizas, que se pueden llevar preparadas y podemos lanzar sin desequipar nuestra arma principal. Podemos beber pociones con efectos positivos o negativos y se pueden leer *pergaminos* de manera similar (excepto con el guerrero, que no sabe leer). Nos entrará el *hambre* a lo largo del tiempo y será necesario comer o llegaremos a un punto en que la vida bajará. También hay *monedas de oro*, aunque de momento sólo sirven para incrementar la puntuación final.

Como en todo *roguelike* clásico, una vez que nuestro personaje *muere*, se acabó, no se puede recuperar una partida guardada, puesto que estas se borran al ser cargadas. Para evitar el aburrimiento de volver a empezar la misma partida una y otra vez, el juego genera proceduralmente los elementos del mundo, de modo que los niveles de la mazmorra, los enemigos, los objetos, etc serán distintos en cada partida, lo que hace que el juego sea mucho más rejugable. Al mismo tiempo, me pareció interesante añadir la opción de generar una partida a partir de una *semilla*. Si se escoge esta opción, el jugador debe introducir un número (la semilla) para generar la partida. Si, tras acabar la partida, vuelve a generar otra partida con la misma semilla, las condiciones iniciales de la partida serán exactamente las mismas. Cualquier persona, en cualquier momento, en cualquier ordenador que use esa semilla empezará la misma partida. Una vez empezada, eso sí, las acciones del jugador harán que el desarrollo cambie, pero si dos jugadores con la misma semilla hacen las mismas pulsaciones en el teclado, las partidas serán idénticas.

Podremos guardar una partida para cargarla luego, y proseguir la partida, aunque al cargar la partida, el fichero en cuestión se borra. De este modo, si bien se puede jugar una partida en varias sesiones, en este juego no se puede guardar al llegar a una situación peligrosa y cargar la partida en caso de morir, por ejemplo.

##El héroe y el combate
Nuestro héroe tiene varias *características principales* que se pueden mejorar al subir nivel, o mediante pociones y pergaminos: *fuerza* (influye al daño y puntos de vida), *destreza* (aumenta la probabilidad de acertar y esquivar ataques), *inteligencia* (permite usar hechizos más avanzados y ampliar su efecto). Cada clase, tiene sus propios puntos fuertes y débiles en cuanto a sus características; el *guerrero* tiene mucha fuerza y mucha vida pero poca destreza, poca inteligencia y no sabe leer ni lanzar hechizos; el arquero tiene una cantidad de vida y fuerza medias, pero mucha destreza; finalmente el mago, tiene poca vida y poca fuerza pero mucha energía mágica, e inteligencia. Con cada clase hay que jugar de una manera diferente. Por supuesto, esto en un futuro puede variar, y se añadirán más clases, características y demás.

También hay otras características secundarias como la velocidad de regeneración de vida o maná, el rango de visión y audición que tiene el personaje, o su comida favorita (que además de quitar el hambre nos curará ligeramente). 

El *combate* es bastante sencillo ahora mismo. Hay una "tirada de dados", por así decirlo, de la agilidad del atacante (más modificador) contra la del atacado (más modificador). Para simplificar, diré ya que todas las tiradas tienen un modificador aleatorio, incluso los grandes héroes fallan algún ataque. El ganador de esa tirada determina si el ataque impacta o es esquivado. Después se calcula el daño recibido en base al arma del atacante, la armadura del atacado, la fuerza de ambos, etc. Es casi seguro que el combate cambie con el tiempo y evolucione a un sistema más complejo, con más variables de por medio, críticos, etc. 

Los *hechizos* necesitarán un mínimo nivel de inteligencia para poder utilizarlos y gastarán maná. Si no tenemos la cantidad necesaria de maná, habrá que esperar hasta que se regenere por sí mismo.

##Ítems

###Armas y armaduras

Todas las *armas* y *armaduras*, excepto las que recibe el héroe al inicio de la partida o al subir nivel, pueden estar malditas. Un *ítem maldito* no podrá ser desequipado hasta que se quite la maldición, bien al subir nivel o con un pergamino. También pueden estar hechas de distintos materiales, siendo un ítem de hierro mejor que uno de madera, etc. Aparte, tienen un modificador oculto que sólo conoceremos tras usar el arma durante un número determinado de 	turnos. Es posible que tengamos un arma +3 en el inventario pero no lo sepamos y mientras tanto estemos 	combatiendo con un arma +1 que es peor. 
	Podemos llevar equipadas armas arrojadizas, que podremos lanzar a nuestros enemigos sin	necesidad de desequipar el arma principal, equipar la arrojadiza, lanzarla y volver a equipar la principal.
	
###Comida
Todo héroe debe *comer* para mantener energías. Nuestro héroe estará progresivamente cada vez más hambriento y como la comida no 	aparece tras la generación inicial de la mazmorra, deberemos avanzar para encontrar más comida 	antes de morir de hambre. Cada acción consumirá más o menos energía y, onsecuentemente. nos dará más o menos hambre; por ejemplo, atacar y fallar a un enemigo, consume más energía que atacar y acertar, que a su vez consume más que dar un paso. Los cadáveres que dejan los enemigos se pueden comer y pueden tener algún efecto sobre el jugador. 
	También hay que tener en cuenta la *comida favorita* de nuestro héroe, que cambiará en cada partida y que nos curará puntos de vida además de saciarnos el hambre. 
	
###Pociones y Pergaminos
Como en todo *roguelike*, al inicio de la partida no sabremos los efectos de las *pociones* y los *pergaminos*. En un principio, sólo reconoceremos las pociones por su color, y los pergaminos por su nombre *extraño*. Al usar uno de estos ítems, si su efecto se hace visible, pasaremos a reconocerlos y en vez de una ''poción roja" veremos una ''poción de curación``, por ejemplo. Por supuesto, el color cambia en cada partida, al igual que los nombres de los pergaminos. Algunos de sus efectos pueden ser negativos o neutrales, que nos beneficiarán o no dependiendo del momento en que se usen (un pergamino de teletransporte, por ejemplo).
	
###Viaje automático e interfaz adaptada

Toda la interfaz ha sido pensada para facilitar la jugabilidad de los jugadores invidentes. Al contrario que otros juegos, que listan en orden alfabético, este juego lista por orden QWERTY, para que así las opciones quedarán siempre ordenadas y juntas en las mismas teclas. 

Asimismo, en los *roguelikes* clásicos, al lanzar una flecha, por ejemplo, había que seleccionar con el teclado la casilla a la que queremos disparar. Este juego nos da directamente una lista de los objetivos a los que nos puede interesar lanzar una flecha (enemigos, de momento), ordenados por distancia, de menor a mayor y donde Q corresponderá siempre el enemigo más cercano.

Se añade también dos teclas para recibir información acerca de los alrededores, dándonos la posición relativa de los elementos a nuestro alrededor; enemigos, objetos, puertas, etc. Tenemos dos opciones: recibir la información en forma de coordenadas cartesianas, siendo nuestra casilla la (0,0); o recibir direcciones en forma de norte, sur, este, oeste, etc.

Se ha creado también un método de *viaje automático*, que permite que el jugador no tenga que recordar dónde están las escaleras en cada nivel. Al pulsar la tecla de viaje aparecen las opciones de viajar a las escaleras de subida/bajada (si ya se han descubierto), de viajar a un sitio inexplorado aleatorio (si queda algo inexplorado). Tras escoger el destino, cada vez que se pulsa la tecla de dar un paso, se avanza una casilla hacia dicho destino. En cualquier momento se pueden pulsar las teclas de descripción para ver lo que hay a nuestro alrededor sin tener que parar de viajar. El viaje parará automáticamente al encontrarnos a un enemigo, también se nos avisará si aparece algún objeto en el suelo que no habíamos visto nunca (se advierte de la aparición de objetos aunque no se esté viajando).

##Controles
Nos podemos mover por el mapa con las teclas WASD correspondientes a las direcciones arriba, izquierda, abajo y derecha y respectivamente QEZC 
para las diagonales, según su colocación en el teclado.
 
Controles:		                         

* [,] para coger cosas           
* [y] para equiparte cosas           
* [h] para comer cosas                  
+ [;] para examinar las cosas del mapa   
+ [i] para examinar tu inventario 
+ [t] para tirar cosas            
* [m] para ver los últimos mensajes				 
* [k] para beber pociones				 
* [l] para leer pergaminos			 
* [x] para viajar 
* [x] para dar un paso durante un viaje
* [p] para guardar la partida
* [g] para lanzar un arma arrojadiza	 
* [f] para disparar tu arma a distancia
* [r] para desequiparte cosas
* [u] para atacar enemigos automáticamente		 
* [v][b] para describir		 
* [<] para subir escaleras			 
* [>] para bajar escaleras