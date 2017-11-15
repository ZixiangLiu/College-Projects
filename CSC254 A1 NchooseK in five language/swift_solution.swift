// CSC254
// A1
// Zixiang Liu
// Partner: Yukun Chen

func combinations(k: Int, n: [Int]) -> [[Int]] {        // Recursive function to calculate combinations k n
    var result: [[Int]] = []
    if k==1{                            // Base case: if k==1, put [n] in result list
        for i in n{
            result.append([i])
        }
    }
    else{                               // Inductive case: for each element i in n, put {n[i], combinations of elements after i with length k-1} in result list
        for i in 0..<n.count{
            for j in combinations(k: k-1, n: Array(n.suffix(from: i+1))){
                result.append([n[i]]+j)
            }
        }
    }
    return result           // Return result list
}

let k : Int = Int(CommandLine.arguments[1])!    // Get cammand line argument k, n
let n : Int = Int(CommandLine.arguments[2])!

var n_array = Array(1...n)              // Convert n in to list [1,2,...,n]

for n in combinations(k: k, n: n_array){        // Call combinations function and print the result
    print(n)
}
