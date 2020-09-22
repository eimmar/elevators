import com.tingco.codechallenge.elevator.model.Elevator

/**
 * Enumeration for describing elevator's direction.
 */
enum class Direction {
    UP, DOWN, NONE
}

data class ElevatorRequest(val requestedFromFloor: Int, val toFloor: Int)
