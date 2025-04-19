# Labs de Concorrente

Laboratórios desenvolvidos para a disciplina de Programação Concorrente com o intuito de compreender e aplicar os conceitos da programação concorrente, sendo esses:

- Threads;
- Semáforos;
- Compartilhamento de memória;
- Problema de produtor/consumidor;
- Exclusão mútua;
- Channels (Go);

## Laboratórios desenvolvidos

### Lab 1

O objetivo deste laboratório é utilizar o conceito de threads para o processamento de diversos arquivos ao mesmo tempo, assim, finalizando o tratamento deles mais rápido, diferentemente da sua versão sequencial.

### Lab 2

Dado um programa de processamento de imagem que tem por objetivo a remoção do ruído das imagens, porém ele processa os pixels da imagem de maneira sequencial, portanto, quanto maior a imagem maior será o tempo demandado para finalizar a remoção, logo, o laboratório tem o objetivo de aplicar a concorrência ao programa de modo que cada thread processe apenas parte da imagem ao invés de apenas uma thread processando a imagem por completo.

### Lab 3

Neste laboratório o objetivo é a implementação do problema do Produtor e Consumidor em que há threads que produzem dados que outras threads consomem, sendo estes dados armazenados em um buffer que é compartilhado tanto pelas produtoras quanto consumidoras, além do conceito de threads foi utilizado semáforos para a proteção da região crítica que é o buffer.

### Lab 4

O laboratório em questão possui um código que realiza o sum de arquivos, para a aplicação de concorrência nele foi necessário o uso de semáforos e threads devido a necessidade de retornar a soma de todos os arquivos bem como atender também um requisito da especificação que é limitar o consumo de memória permitindo apenas 50% das threads trabalhando ao mesmo tempo.

### Lab 5

Laboratório desenvolvido apenas para ter o primeiro contato com Go e sua maneira de implementar concorrência.

### Lab 6

Para este laboratório foram desenvolvidos programas com o intuito de aplicar o conceito dos channels utilizados na linguagem Go para que as threads consigam compartilhar informações.

### Lab 7

Com este laboratório outro conceito de Go foi utilizado, sendo este o select que, ao aplicar no progama, permite que o programa leve menos tempo para encerrar seu processamento.

### Lab 8

Este laboratório teve por objetivo o uso da estrutura de dados ExecutorService do java de modo a simplificar o gerenciamento do uso de threads, desenvolvendo alguns programas com essa estrutura para a compreensão da mesma.

### Lab 9

Para o desenvolvimento deste laboratório foi apresentado a interface BlockingQueue do java, bem como suas estruturas thread-safe simplificando o compartilhamento de memória pelas threads utilizando estruturas próprias para esta finalidade.

## Desenvolvido por

<table>
    <tr>
      <td align="center" width="190px" height="160px">
         <img src="https://github.com/alinebmr.png" alt="Aline Profile Image" width="60"></img>
         </br>
         <a href="https://github.com/alinebmr">@alinebmr</a>
         <br>Aline Brito</br>
      </td>
      <td align="center" width="190px" height="160px">
         <img src="https://github.com/FLuiz22.png" alt="Filipe Luiz Profile Image" width="60"></img>
         </br>
         <a href="https://github.com/FLuiz22">@FLuiz22</a>
         <br>Filipe Luiz</br>
      </td>
   </tr>
</table>
