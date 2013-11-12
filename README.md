forcamulticast
==============

Jogo de forca baseado em comunicação multi-cast. 


- Inicialmente, quatro jogadores estabelecem uma comunicação previamente determinada via multi-cast;
- Ao ser constatado que existem 4 processos se comunicando via multi-cast, um deles é eleito para ser o servidor (via eleição por maior ID) e os outros se tornam clientes;
- O servidor gera uma palavra aleatória para ser adivinhada pelos clientes;
- Caso o servidor caia, um novo servidor é eleito e outro processo se torna o servidor;


Observações
- Cada processo tem uma chave privada;
- Multicast fornece: IP, ID e nome de cada processo;
