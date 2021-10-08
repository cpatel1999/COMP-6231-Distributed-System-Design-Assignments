
/*

References : https://mpitutorial.com/tutorials/mpi-send-and-receive/
             https://pdc-support.github.io/introduction-to-mpi/07-collective/index.html
             http://condor.cc.ku.edu/~grobe/docs/intro-MPI-C.shtml
             http://www.math.utep.edu/Faculty/yi/CPS5195f10/mpi_presentation.pdf
             https://en.wikipedia.org/wiki/Message_Passing_Interface
*/

#include <stdlib.h>
#include <stdio.h>
#include "mpi.h"
#include <time.h>

// Number of rows and columnns in a matrix
#define N 8

MPI_Status status;

double a[N][4], b[4][N], c[N][N];

int main(int argc, char** argv)
{
    int size, rank, slaveProcesses, source, dest, rows, partition, i;
    double t2, t3, t4, t5;

    // Initialization of MPI environment.
    MPI_Init(&argc, &argv);
    // Stores rank of each process. Rank is used to uniquely identify each process.
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    // Number of processes involved in entire communication.
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    
    // Number of slave processes.
    slaveProcesses = size - 1;

    if (!(N % slaveProcesses == 0))
    {
        if (rank == 0)
        {
            printf("Invalid number of processes");
        }
        MPI_Finalize();
    }
    // Master process
    else {


        if (rank == 0) {

            // Matrix A and Matrix B both will be filled with random numbers
            srand(time(NULL));
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < 4; j++) {
                    a[i][j] = rand() % 10;
                }
            }

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < N; j++) {
                    b[i][j] = rand() % 10;
                }
            }

            printf("\n\t\tMatrix Multiplication using MPI\n");

            // Print Matrix A
            printf("\nMatrix A\n\n");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < 4; j++) {
                    printf("%.0f\t", a[i][j]);
                }
                printf("\n");
            }

            // Print Matrix B
            printf("\nMatrix B\n\n");
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < N; j++) {
                    printf("%.0f\t", b[i][j]);
                }
                printf("\n");
            }

            rows = N / slaveProcesses;
            partition = 0;

            //Time when master starts transferring data.
            double t1 = MPI_Wtime();
            printf("Time when master starts sending data : %f", t1);

            //Master sends data to slave processes and waits for the result.
            for (i = 1; i <= slaveProcesses; i++)
            {
                MPI_Send(&partition, 1, MPI_INT, i, 1, MPI_COMM_WORLD);
                MPI_Send(&rows, 1, MPI_INT, i, 1, MPI_COMM_WORLD);
                MPI_Send(&a[partition][0], rows * N, MPI_DOUBLE, i, 1, MPI_COMM_WORLD);
                MPI_Send(&b, N * N, MPI_DOUBLE, i, 1, MPI_COMM_WORLD);
                t2 = MPI_Wtime();
                MPI_Send(&t2, 1, MPI_DOUBLE, i, 1, MPI_COMM_WORLD);

                partition = partition + rows;
            }

            for (int j = 1; j <= slaveProcesses; j++)
            {
                source = j;
                MPI_Recv(&partition, 1, MPI_INT, source, 2, MPI_COMM_WORLD, &status);
                MPI_Recv(&rows, 1, MPI_INT, source, 2, MPI_COMM_WORLD, &status);
                MPI_Recv(&c[partition][0], rows * N, MPI_DOUBLE, source, 2, MPI_COMM_WORLD, &status);
                MPI_Recv(&t4, 1, MPI_DOUBLE, source, 2, MPI_COMM_WORLD, &status);
                t5 = MPI_Wtime();
                //printf("Time consumed to reach the result to master %d :%f\n",rank,t5 - t4);

            }

            //Time when master receives all data
            double t2 = MPI_Wtime();
            printf("\n");
            printf("Time when master received all data : %f", t2);
            //Total time for entire communication
            printf("\nTime consumed between sending and receiving data at master process : %f", t2 - t1);

            // Print the result matrix
            printf("\nResult Matrix C = Matrix A * Matrix B:\n\n");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++)
                    printf("%.0f\t", c[i][j]);
                printf("\n");
            }
            printf("\n");
        }

        // Slave Processes 
        if (rank > 0) {

            source = 0;
            MPI_Recv(&partition, 1, MPI_INT, source, 1, MPI_COMM_WORLD, &status);
            MPI_Recv(&rows, 1, MPI_INT, source, 1, MPI_COMM_WORLD, &status);
            MPI_Recv(&a, rows * N, MPI_DOUBLE, source, 1, MPI_COMM_WORLD, &status);
            MPI_Recv(&b, N * N, MPI_DOUBLE, source, 1, MPI_COMM_WORLD, &status);
            MPI_Recv(&t2, 1, MPI_DOUBLE, source, 1, MPI_COMM_WORLD, &status);
            t3 = MPI_Wtime();
           // printf("Time consumed between reaching message from master to slave %d :%f\n",rank, t3 - t2);
            // Matrix multiplication
            for (int k = 0; k < N; k++) {
                for (int i = 0; i < rows; i++) {
                    // Set initial value of the row summataion
                    c[i][k] = 0.0;
                    // Matrix A's element(i, j) will be multiplied with Matrix B's element(j, k)
                    for (int j = 0; j < 4; j++)
                        c[i][k] = c[i][k] + a[i][j] * b[j][k];
                }
            }
            t4 = MPI_Wtime();
           // printf("Time consumed by slave process to perform computations :%f\n",t4-t3);
            MPI_Send(&partition, 1, MPI_INT, 0, 2, MPI_COMM_WORLD);
            MPI_Send(&rows, 1, MPI_INT, 0, 2, MPI_COMM_WORLD);
            MPI_Send(&c, rows * N, MPI_DOUBLE, 0, 2, MPI_COMM_WORLD);
            MPI_Send(&t4, 1, MPI_DOUBLE, 0, 2, MPI_COMM_WORLD);
        }

        MPI_Finalize();
    }
}