<?php include_once 'includes/Utils.php'; ?>

<?php
if (!isset($_SESSION['userId'])) {
    header( 'Location: giris.php?yon=' . thisPage());
}
?>